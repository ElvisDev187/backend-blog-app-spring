package com.elvis.blog.Security;

import com.elvis.blog.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
       try{
           String token  = extractToken(request);
           if (token != null) {
               UserDetails userDetails = authenticationService.validateToken(token);

               UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(
                       userDetails,
                       null,
                       userDetails.getAuthorities()
               );

               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
               if(userDetails instanceof BlogUserDetails){
                   request.setAttribute("userId",( (BlogUserDetails) userDetails).getId());
               }
           }
       } catch (Exception e) {
           //Don't trow exception just don't authenticate the user
           log.warn("Receive invalid token");
       }
       filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String BearerToken = request.getHeader("Authorization");
        if (BearerToken != null && BearerToken.startsWith("Bearer ")) {
            return BearerToken.substring(7);
        }
        return null;
    }
}
