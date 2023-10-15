package br.com.felipe.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.felipe.todolist.user.repository.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class FilterTaskAuth extends OncePerRequestFilter {

    private final IUserRepository userRepository;

    // go through here before going to the controllers
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        var servletPath = request.getServletPath();
        if (servletPath.startsWith("/api/v1/tasks/")) {
            String authorization = request.getHeader("Authorization");
            String authEncoded = authorization.substring("Basic".length()).trim();
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
            String authString = new String(authDecoded);

            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            var user = this.userRepository.findByUsername(username);
            if (user.isEmpty()) {
                response.sendError(HttpStatus.FORBIDDEN.value());
            } else {
                var passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), user.get().getPassword());
                if (passwordVerified.verified) {
                    request.setAttribute("userId", user.get().getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(HttpStatus.FORBIDDEN.value());
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }
}
