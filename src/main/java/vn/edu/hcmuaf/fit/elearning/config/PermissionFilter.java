package vn.edu.hcmuaf.fit.elearning.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.edu.hcmuaf.fit.elearning.feature.auth.cache.PermissionCacheDto;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.PermissionEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.repository.PermissionRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionFilter extends OncePerRequestFilter {
    private final PermissionRepository permissionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY_PREFIX = "permissions:";
    private static final long CACHE_TTL_MINUTES = 60; // Cache sống trong 60 phút

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Check if the user is authenticated
        if (authentication == null) {
            // If not authenticated, proceed to the next filter
            filterChain.doFilter(request, response);
            return;
        }
        String requestURI = request.getRequestURI();
        String method = request.getMethod();


         if (!hasPermission(authentication, requestURI, method)) {
             response.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
             return;
         }

        // If authenticated and has permission, proceed to the next filter
        filterChain.doFilter(request, response);
    }

    private boolean hasPermission(Authentication authentication, String requestURI, String method) {
        // Lấy danh sách vai trò từ Authentication
        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        log.debug("User roles: {}", userRoles);
        // Lấy danh sách quyền từ cache hoặc cơ sở dữ liệu
        List<PermissionCacheDto> permissions = getPermissionsForRoles(userRoles);
        // Kiểm tra xem có quyền nào khớp với requestURI và method không
        boolean hasPermission = permissions.stream().anyMatch(permission ->
                matchesPath(permission.getPath(), requestURI) &&
                        permission.getMethod().name().equals(method)
        );
        log.debug("Permission check for {} {}: {}", method, requestURI, hasPermission);
        return hasPermission;
    }

    private List<PermissionCacheDto> getPermissionsForRoles(Set<String> roleNames) {
        List<PermissionCacheDto> allPermissions = new ArrayList<>();

        for (String roleName : roleNames) {
            String cacheKey = CACHE_KEY_PREFIX + roleName;
            // Kiểm tra cache
            Object cachedData = redisTemplate.opsForValue().get(cacheKey);
            List<PermissionCacheDto> cachedPermissions = (List<PermissionCacheDto>) cachedData;
            if (cachedPermissions != null) {
                log.debug("Cache hit for role: {}", roleName);
                allPermissions.addAll(cachedPermissions);
                continue;
            }

            // Cache miss, truy vấn cơ sở dữ liệu
            log.debug("Cache miss for role: {}, querying database", roleName);
            List<PermissionEntity> dbPermissions = permissionRepository.findByRoles_Name(roleName);
            List<PermissionCacheDto> dtos = dbPermissions.stream().map(p ->
                    PermissionCacheDto.builder()
                            .path(p.getPath())
                            .method(p.getMethod())
                            .module(p.getModule())
                            .description(p.getDescription())
                            .build()
            ).toList();
            allPermissions.addAll(dtos);

            // Lưu vào cache với TTL
            redisTemplate.opsForValue().set(cacheKey, dtos, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
        }
        return allPermissions;
    }
    private boolean matchesPath(String permissionPath, String requestURI) {
        if (permissionPath.endsWith("/**")) {
            String basePath = permissionPath.substring(0, permissionPath.length() - 3);
            return requestURI.startsWith(basePath);
        }else if( permissionPath.indexOf(":") > 0){
            String[] parts = permissionPath.split(":");
            if (parts.length == 2) {
                String basePath = parts[0];
                String variablePart = parts[1];
                return requestURI.startsWith(basePath);
            }
        }
        return permissionPath.equals(requestURI);
    }
}
