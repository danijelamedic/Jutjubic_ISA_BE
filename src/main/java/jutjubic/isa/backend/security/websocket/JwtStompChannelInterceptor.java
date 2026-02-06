package jutjubic.isa.backend.security.websocket;

import jutjubic.isa.backend.security.JwtService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtStompChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtStompChannelInterceptor(JwtService jwtService,
                                      UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = firstNativeHeader(accessor, "Authorization");
            if (authHeader == null) authHeader = firstNativeHeader(accessor, "authorization");
            if (authHeader == null) authHeader = firstNativeHeader(accessor, "AUTHORIZATION");

            System.out.println("[WS CONNECT] nativeHeaders=" + accessor.toNativeHeaderMap());

            if (authHeader != null && !authHeader.isBlank()) {

                String token = authHeader.startsWith("Bearer ")
                        ? authHeader.substring(7)
                        : authHeader;

                try {
                    String usernameOrEmail = jwtService.extractEmail(token);
                    if (usernameOrEmail != null && jwtService.isTokenValid(token,userDetailsService.loadUserByUsername(usernameOrEmail))) {

                        UserDetails ud = userDetailsService.loadUserByUsername(usernameOrEmail);

                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                ud, null, ud.getAuthorities()
                        );

                        accessor.setUser(auth);

                        System.out.println("[WS CONNECT]  authenticated user=" + auth.getName());
                    } else {
                        System.out.println("[WS CONNECT]  token invalid / username null");
                    }
                } catch (Exception e) {
                    System.out.println("[WS CONNECT]  exception: " + e.getMessage());
                }
            } else {
                System.out.println("[WS CONNECT]  no Authorization header");
            }
        }

        return message;
    }

    private String firstNativeHeader(StompHeaderAccessor accessor, String name) {
        List<String> values = accessor.getNativeHeader(name);
        return (values == null || values.isEmpty()) ? null : values.get(0);
    }
}
