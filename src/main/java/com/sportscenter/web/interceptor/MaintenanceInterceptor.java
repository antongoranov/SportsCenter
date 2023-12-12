package com.sportscenter.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class MaintenanceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        boolean maintenanceMode = underMaintenance();

        if (maintenanceMode && !requestURI.equals("/maintenance")) {
            response.sendRedirect("/maintenance"); // Redirect to maintenance page
            return false;
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean underMaintenance() {
        return LocalDateTime.now().getHour() == 23 &&
                LocalDateTime.now().getDayOfWeek() == DayOfWeek.SUNDAY;

    }
}
