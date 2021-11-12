package am.aca.generactive.controller;

import am.aca.generactive.controller.vm.UsernamePasswordVM;
import am.aca.generactive.security.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.stream.Collectors;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody UsernamePasswordVM credentials) {
        // Manually create authentication token (also a chile of Authentication)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());

        // Authenticate given authentication
        // This will try to find user, and if credentials match, then authenticate the user.
        // If credentials are wrong, than a BadCredentialsException will be thrown
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // Stores authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return createToken(authentication);
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date validity = new Date(now + (125 * 60 * 1000));

        return tokenProvider.createToken(authentication.getName(), authorities, validity);
    }
}
