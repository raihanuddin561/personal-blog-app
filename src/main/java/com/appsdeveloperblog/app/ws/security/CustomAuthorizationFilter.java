package com.appsdeveloperblog.app.ws.security;

import com.appsdeveloperblog.app.ws.constants.SecurityConstants;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        if(request.getServletPath().equals(SecurityConstants.LOGIN)){
            filterChain.doFilter(request,response);
        }else{
            String authHeader = request.getHeader(SecurityConstants.HEADER_STRING);
            if(authHeader == null || !authHeader.startsWith(SecurityConstants.TOKEN_PREFIX)){
               filterChain.doFilter(request,response);
            }
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request,response);
        }
    }
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        if(token!=null){
            token = token.substring(SecurityConstants.TOKEN_PREFIX.length());
            String user = Jwts.parser()
                    .setSigningKey(SecurityConstants.getTokenSecret())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
           if(user!=null){
               return new UsernamePasswordAuthenticationToken(user,null,new ArrayList<>());
           }
           return null;
        }
    return null;
    }
}
