package ge.chalauri.fantasy.security.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import ge.chalauri.fantasy.model.request.SignupRequest;
import ge.chalauri.fantasy.model.response.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static ge.chalauri.fantasy.security.utils.SecurityConstants.HEADER_STRING;
import static ge.chalauri.fantasy.security.utils.SecurityConstants.SECRET;
import static ge.chalauri.fantasy.security.utils.SecurityConstants.TOKEN_PREFIX;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                   long tokenExpirationInMs) {
        this.authenticationManager = authenticationManager;
        this.tokenExpirationInMs = tokenExpirationInMs;
    }

    private long tokenExpirationInMs;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            SignupRequest creds = new ObjectMapper()
                .readValue(req.getInputStream(), SignupRequest.class);

            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    creds.getUsername(),
                    creds.getPassword(),
                    new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        String token = JWT.create()
                          .withSubject(((UserDetailsImpl) auth.getPrincipal()).getUsername())
                          .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationInMs))
                          .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        out.println(objectMapper.writeValueAsString(auth.getPrincipal()));
        out.flush();
    }
}
