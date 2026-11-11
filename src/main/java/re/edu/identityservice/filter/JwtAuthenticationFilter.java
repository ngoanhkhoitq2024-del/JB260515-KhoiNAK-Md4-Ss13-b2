package re.edu.identityservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import re.edu.identityservice.util.JwtUtil;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Lấy JWT từ HTTP Header
        String authHeader = request.getHeader("Authorization");

        // 2. Kiểm tra Header có chứa Bearer Token không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Tách JWT ra khỏi chuỗi Bearer
        String token = authHeader.substring(7);

        // 4. Kiểm tra JWT hợp lệ
        if (jwtUtil.validateToken(token)) {

            // 5. Lấy thông tin từ JWT
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            // 6. Tạo quyền cho người dùng
            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + role);

            // 7. Tạo đối tượng Authentication
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(authority)
                    );

            // 8. Lưu thông tin xác thực vào SecurityContext
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        // 9. Tiếp tục chuỗi Filter
        filterChain.doFilter(request, response);
    }
}
