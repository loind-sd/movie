package com.cinema.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
public class SimpleRequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // ❌ Skip multipart
        if (request.getContentType() != null
                && request.getContentType().startsWith("multipart/")) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        logBasicInfo(wrappedRequest);

        filterChain.doFilter(request, response);
    }

    private String maskSensitive(String body) {
        return body
                .replaceAll("(\"password\"\\s*:\\s*\")[^\"]+\"", "$1***\"")
                .replaceAll("(\"token\"\\s*:\\s*\")[^\"]+\"", "$1***\"");
    }

    private void logBasicInfo(ContentCachingRequestWrapper request) {
        StringBuilder logMsg = new StringBuilder("\n========== Incoming Request ==========\n");
        logMsg.append("Timestamp: ").append(LocalDateTime.now()).append("\n");
        logMsg.append("Method: ").append(request.getMethod()).append("\n");
        logMsg.append("URI: ").append(request.getRequestURI()).append("\n");

        String clientIp = getClientIp(request);
        logMsg.append("Client IP: ").append(clientIp).append("\n");
        logMsg.append("Remote Host: ").append(request.getRemoteHost()).append("\n");

        // ---- Parameters ----
        logMsg.append("Parameters:\n");
        Map<String, String[]> params = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            logMsg.append("  ").append(entry.getKey()).append(": ")
                    .append(String.join(",", entry.getValue())).append("\n");
        }

        logMsg.append("======================================");
        log.info(logMsg.toString());
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}

