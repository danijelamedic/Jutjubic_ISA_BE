# Jutjubić (ISA) - Backend (Spring Boot + PostgreSQL)

Ovaj repozitorijum sadrži backend deo projekta **Jutjubić** rađen za predmet *Internet softverske arhitekture* (ISA).
Backend je implementiran u **Spring Boot** uz **PostgreSQL**, **JWT** autentifikaciju, keširanje, transakcioni upload fajlova i integraciju sa **RabbitMQ** (JSON & Protobuf), kao i **Watch Party** funkcionalnost preko **WebSocket/STOMP**.

---

## Pokrivene funkcionalnosti (po specifikaciji)

U ovom backendu implementirane su sledeće tačke :

- **3.1** Prikaz informacija neautentifikovanim korisnicima (public feed + public profili)
- **3.2** Registracija + aktivacija naloga + login (JWT) + rate limit po IP (5/min)
- **3.3** Kreiranje video objave (multipart upload, lokalno skladištenje, transakcioni rollback, cache thumbnail-a)
- **3.6** Komentarisanje videa (paginacija, cache, rate limit 60/h po nalogu + test/simulacija)
- **3.7** Brojač pregleda (konzistentan pri konkurentnim posetama + test/simulacija)
- **3.14** MQ JSON vs Protobuf (publish događaja pri upload-u + NotifyMe integracija)
- **3.15** Watch Party (sobe + real-time start video preko WebSocket-a, testirano na 2 računara)

---

## Tehnologije

- Java + Spring Boot
- Spring Security + JWT (stateless)
- Spring Data JPA (PostgreSQL)
- Bean Validation (jakarta.validation)
- Spring Cache (keširanje thumbnail-a i komentara)
- RabbitMQ (AMQP) - JSON i Protobuf poruke
- WebSocket + STOMP (Watch Party)

---

## Preduslovi

Instalirano / dostupno:
- **JDK 17+**
- **PostgreSQL**
- **RabbitMQ (Docker preporučeno)**

---

## Konfiguracija (application.properties)

Tipično se podešava:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- JWT secret / expiration (ako imate u properties)
- putanje za lokalno čuvanje fajlova (video + thumbnail)
- RabbitMQ host/port/user/pass

> Napomena: Backend čuva video/thumbnail **lokalno na serveru** (file system).

---

## Pokretanje baze (PostgreSQL)

Pokreni PostgreSQL lokalno ili preko Docker-a.

Primer (Docker):
```
docker run --name jutjubic-postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=jutjubic \
  -p 5432:5432 -d postgres:16
```
## Pokretanje RabbitMQ (za 3.14)

Preporuka: RabbitMQ management image.

```docker run --name rabbitmq -p 5672:5672 -p 15672:15672 -d rabbitmq:3-management```


RabbitMQ UI:

```http://localhost:15672```

Default login: guest / guest

Pokretanje aplikacije

Ako koristite Maven wrapper:

```./mvnw spring-boot:run```

Ili iz IDE (IntelliJ):
Run glavnu Spring Boot klasu.

---

## Implementacija po tačkama

---

### 3.1 Prikaz informacija neautentifikovanim korisnicima

#### Šta je implementirano

- Neautentifikovani korisnici imaju pristup sledećim **javnim endpointima**:
  - pregled liste video objava (sortirano po vremenu nastanka, najnovije prvo)
  - stranici za pregled pojedinačnog videa
  - stranici za pregled javnog profila korisnika (autora videa ili komentara)
  - čitanju komentara ispod video objava

- Neautentifikovani korisnici **nemaju mogućnost** da:
  - ostavljaju komentare
  - lajkuju video objave
  - postavljaju nove video objave
  - pristupaju zaštićenim delovima sistema

- Pri pokušaju lajkovanja ili komentarisanja bez autentifikacije, korisnik je obavešten da je potrebno da se prijavi na sistem.

#### Server-side zaštita

- Spring Security konfiguracija je podešena tako da su javno dostupni isključivo sledeći endpointi:
  - `/api/public/**`
  - `/api/auth/**`

- Svi ostali REST endpointi zahtevaju validan JWT token.

- WebSocket endpoint je dostupan za inicijalni handshake, dok se autentifikacija korisnika za Watch Party događaje vrši putem JWT tokena prosleđenog u STOMP `CONNECT` headeru.


### 3.2 Registracija korisnika i prijava na sistem

#### Šta je implementirano

- Neautentifikovanim korisnicima je uvek dostupan pristup stranicama za:
  - registraciju naloga
  - prijavu na sistem

- **Registracija korisnika** obuhvata unos sledećih podataka:
  - email adresa
  - korisničko ime
  - lozinka
  - potvrda lozinke
  - ime
  - prezime
  - adresa

- Implementirane su sve neophodne **server-side validacije**, uključujući:
  - format email adrese
  - jedinstvenost email adrese i korisničkog imena
  - minimalne zahteve za lozinku
  - poklapanje lozinke i potvrde lozinke

- Lozinke se **nikada ne čuvaju u čistom tekstu**, već se hash-uju korišćenjem **BCrypt** algoritma.

#### Aktivacija naloga

- Nakon uspešne registracije, korisniku se generiše **aktivacioni token** koji se šalje na email adresu korisnika.
- Nalog se inicijalno kreira kao **neaktivan (`enabled = false`)**.
- Korisnik **ne može da se prijavi na sistem** dok ne aktivira nalog putem dobijenog aktivacionog linka.
- Aktivacioni token ima ograničeno vreme važenja.

#### Prijava na sistem (Login)

- Prijava na sistem se vrši korišćenjem:
  - email adrese
  - lozinke

- Nakon uspešne autentifikacije, sistem izdaje **JWT token** koji se koristi za autorizaciju svih zaštićenih zahteva.
- Aplikacija koristi **stateless** autentifikaciju (bez serverske sesije).

#### Ograničenje broja pokušaja prijave (Rate limiting)

- Implementirano je ograničenje broja pokušaja prijave na osnovu IP adrese:
  - **maksimalno 5 pokušaja prijave po minuti po IP adresi**

- U slučaju prekoračenja dozvoljenog broja pokušaja, dalji zahtevi za prijavu se privremeno odbijaju.

#### Server-side autentifikacija i autorizacija


### 3.3 Kreiranje video objave

#### Šta je implementirano

- Registrovani (autentifikovani) korisnici imaju mogućnost da kreiraju nove video objave.
- Kreiranje video objave zahteva sledeće podatke:
  - naslov videa
  - opis videa
  - sliku koja predstavlja video (thumbnail)
  - video fajl u **mp4** formatu (maksimalne veličine **200MB**)
  - vreme kreiranja objave (postavlja se sistemski)
  - geografska lokacija (opciono)

- Podaci se na backend šalju korišćenjem **multipart/form-data** zahteva, gde se:
  - metapodaci o objavi šalju kao JSON deo zahteva
  - video i thumbnail šalju kao fajl delovi zahteva

#### Lokalno skladištenje fajlova

- Video fajlovi i thumbnail slike se čuvaju **lokalno na serveru** (file system).
- Pre čuvanja fajlova, vrše se validacije:
  - format video fajla (dozvoljen isključivo mp4)
  - maksimalna veličina video fajla (≤ 200MB)

#### Transakciona obrada i rollback

- Proces kreiranja video objave je implementiran **transakciono**.
- Ukoliko dođe do greške tokom:
  - upload-a video fajla
  - upload-a thumbnail slike
  - obrade podataka o objavi
  - prekoračenja dozvoljenog vremena za upload

  izvršava se **kompletan rollback operacije**, pri čemu se:
  - ne kreira zapis u bazi
  - privremeno sačuvani fajlovi uklanjaju sa sistema

- Na ovaj način se obezbeđuje konzistentnost između baze podataka i file sistema.

#### Dostupnost i keširanje thumbnail-a

- Nakon uspešnog kreiranja, nova video objava je odmah dostupna drugim korisnicima za:
  - pregled
  - lajkovanje
  - komentarisanje

- Thumbnail slike se **keširaju na backendu**, čime se izbegava ponovno čitanje slike sa file sistema prilikom svakog zahteva za prikaz.

#### Napomena

- Backend podržava strukturu za rad sa tagovima, ali korisnički interfejs može biti pojednostavljen bez eksplicitnog unosa tagova, u skladu sa fokusom na funkcionalnu implementaciju zahteva.

- Za autentifikaciju i autorizaciju koristi se **Spring Security**.
- Svi zaštićeni endpointi zahtevaju validan JWT token.
- Autorizacija je dosledno primenjena na serverskoj strani za sve funkcionalnosti sistema, nezavisno od klijentske logike.

### 3.6 Postupak komentarisanja videa

#### Šta je implementirano

- Samo **registrovani (autentifikovani) korisnici** imaju mogućnost ostavljanja komentara na video objavama.
- Komentar može da sadrži **isključivo tekstualni sadržaj**.
- Svaki komentar čuva sledeće informacije:
  - tekst komentara
  - nalog korisnika koji je komentar postavio
  - vreme kreiranja komentara

- Komentari se na stranici video objave prikazuju **sortirani od najnovijeg ka najstarijem**.

#### Keširanje i paginacija

- Komentari su **keširani na backendu**, čime se smanjuje opterećenje baze podataka pri čitanju.
- U slučaju velikog broja komentara, komentari se vraćaju kroz **paginaciju**, korišćenjem `Page` mehanizma iz Spring Data JPA.

#### Ograničenje broja komentara po nalogu (Rate limiting)

- Implementirano je ograničenje broja komentara koje jedan korisnik može da postavi:
  - **maksimalno 60 komentara u vremenskom intervalu od jednog sata po nalogu**

- Ograničenje važi na nivou korisničkog naloga, nezavisno od broja video objava koje korisnik komentariše.

#### Demonstracija mehanizma

- Radi demonstracije ispravnosti mehanizma za ograničavanje komentara, implementiran je:
  - test ili skripta koja simulira slanje velikog broja komentara u kratkom vremenskom periodu

- Test potvrđuje da sistem ispravno odbija zahteve koji prekoračuju dozvoljeni limit.


### 3.7 Brojač pregleda video objava

#### Šta je implementirano

- Za svaku video objavu vodi se evidencija o **ukupnom broju pregleda**.
- Brojač pregleda se uvećava **svaki put kada korisnik pristupi stranici za pregled videa**, nezavisno od toga da li je korisnik autentifikovan ili ne.

#### Konzistentnost pri istovremenim posetama

- Inkrement broja pregleda je implementiran tako da bude **konzistentan u uslovima konkurentnog pristupa** istom video sadržaju.
- Rešenje je otporno na istovremene zahteve više korisnika i sprečava gubitak inkrementa pregleda.

#### Demonstracija mehanizma

- Radi demonstracije ispravnosti implementacije, pripremljen je:
  - test ili skripta koja simulira **istovremenu posetu istom videu od strane više korisnika**
 
 ### 3.14 MQ – JSON vs Protobuf

#### Šta je implementirano

- Implementirana je integracija sa **message queue** sistemom (RabbitMQ) za slanje poruka prilikom uspešnog kreiranja nove video objave.
- Svaki put kada se video objava uspešno kreira, emituje se događaj tipa **UploadEvent** koji sadrži osnovne informacije o novokreiranom videu, kao što su:
  - naziv videa
  - autor videa
  - veličina video fajla
  - vreme kreiranja događaja

#### Formati poruka

- Slanje poruka je implementirano u **dva formata**:
  - **JSON**, korišćenjem standardne serijalizacije
  - **Protobuf**, korišćenjem definisane `.proto` šeme i generisanih klasa

- Za svaki format poruka koristi se **odvojeni routing key i queue**, čime se sprečava mešanje različitih tipova poruka.

#### Pouzdanost slanja

- Poruka se šalje **isključivo nakon uspešnog commit-a transakcije** kreiranja video objave.
- Na ovaj način se obezbeđuje da se poruke ne emituju u slučaju neuspešnog upload-a ili rollback-a operacije.

#### Poređenje JSON i Protobuf formata

- Izvršeno je poređenje JSON i Protobuf formata na osnovu sledećih kriterijuma:
  - prosečno vreme serijalizacije
  - prosečno vreme deserijalizacije
  - veličina poruke

- Poređenje je izvršeno na **najmanje 50 poruka** za svaki format.
- Rezultati poređenja jasno pokazuju razlike u performansama i veličini poruka između JSON i Protobuf formata.

#### Napomena

- Aplikacija koja konzumira poruke (NotifyMe servis) implementirana je kao **poseban mikroservis** i nalazi se u posebnom repozitorijumu.


- Test potvrđuje da se broj pregleda pravilno uvećava i da je krajnja vrednost u skladu sa brojem izvršenih zahteva.

### 3.15 Watch Party

#### Šta je implementirano

- Implementirana je funkcionalnost **Watch Party** koja omogućava više korisnika da istovremeno gledaju isti video sadržaj.
- Samo **autentifikovani korisnici** mogu da kreiraju Watch Party sobu.
- Ostali autentifikovani korisnici mogu da:
  - vide listu aktivnih Watch Party soba
  - pridruže se postojećoj Watch Party sobi

#### Pokretanje videa i sinhronizacija

- Kreator (vlasnik) Watch Party sobe ima mogućnost da pokrene video.
- U trenutku kada vlasnik pokrene video, svim ostalim članovima sobe se u realnom vremenu šalje događaj za pokretanje videa.
- Klijentska aplikacija automatski otvara stranicu za pregled istog videa kod svih članova sobe.

- **Nije implementirana sinhronizacija play/pause kontrole niti trenutne pozicije videa**, u skladu sa specifikacijom - cilj je isključivo otvaranje istog video sadržaja u realnom vremenu.

#### WebSocket komunikacija i autentifikacija

- Za real-time komunikaciju koristi se **WebSocket** uz **STOMP** protokol.
- Autentifikacija korisnika u okviru WebSocket komunikacije vrši se pomoću **JWT tokena**, koji se prosleđuje u STOMP `CONNECT` headeru.
- Na backend strani se JWT token validira i mapira na korisnički `Principal`.

#### Testiranje funkcionalnosti

- Watch Party funkcionalnost je testirana na **dva računara**:
  - na prvom računarom je kreirana Watch Party soba i pokrenut video
  - na drugom računaru je korisnik automatski preusmeren na isti video u realnom vremenu


