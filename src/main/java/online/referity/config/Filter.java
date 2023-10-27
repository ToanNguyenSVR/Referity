package online.referity.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import online.referity.dto.UserSecurityDTO;
import online.referity.exception.exceptions.ExpiredToken;
import online.referity.exception.exceptions.InValidToken;
import online.referity.service.UserSecurityService;
import online.referity.utils.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    TokenHandler tokenHandler;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Autowired
    UserSecurityService userSecurityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = getURI(request);
        filterChain.doFilter(request, response);
//        if (uri.contains("/login") || uri.contains("/register") || uri.contains("swagger-ui") || uri.contains("v3") || uri.contains("job")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = getToken(request);
//        if (token == null) {
//            resolver.resolveException(request, response, null, new InValidToken("Empty Token!"));
//        } else {
//            String id;
//            try {
//                id = tokenHandler.getInfoByToken(token);
//            } catch (ExpiredJwtException expiredJwtException) {
//                resolver.resolveException(request, response, null, new ExpiredToken("Expired Token!"));
//                return;
//            } catch (MalformedJwtException malformedJwtException) {
//                resolver.resolveException(request, response, null, new InValidToken("Invalid Token!"));
//                return;
//            }
//
//            if (id != null) {
//                // token chuẩn
//                // tạo 1 đối tượng mà spring security hiểu
//                UserSecurityDTO userDetails = (UserSecurityDTO) userSecurityService.loadUserById(id);
//                //token hop le
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                // chạy đc tới đây => set up cho thằng spring security ok hết r
//                // truy cập vào chạy api
//                // filter cho phép truy cập thì mới đc truy cập
//                filterChain.doFilter(request, response);
//            } else {
//                // token tào lao
//                resolver.resolveException(request, response, null, new InValidToken("Invalid Token!"));
//            }
//        }

//        filterChain.doFilter(request, response);
    }

    String getURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    private String getToken(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");
        if (authorization == null)
            return null;

        String token = authorization.split(" ")[1];
        return token;
    }
}
