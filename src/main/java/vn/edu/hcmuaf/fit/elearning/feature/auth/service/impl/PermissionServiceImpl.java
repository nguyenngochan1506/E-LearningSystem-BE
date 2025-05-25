package vn.edu.hcmuaf.fit.elearning.feature.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.PermissionCreationRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.PermissionUpdateRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.PermissionPageResponse;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.PermissionResponse;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.PermissionEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.RoleEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.repository.PermissionRepository;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.PermissionService;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY_PREFIX = "permissions:";
    @Override
    public long createPermission(PermissionCreationRequest request) {
        log.info("Create permission {}", request.getModule());
        PermissionEntity entity = new PermissionEntity();
        entity.setDescription(request.getDescription());
        entity.setPath(request.getPath());
        entity.setModule(request.getModule().toUpperCase());
        entity.setMethod(request.getMethod());
        entity.setDeleted(false);
        // save to db
        invalidateCacheForRoles(entity.getRoles());
        log.info("Permission created");
        return permissionRepository.save(entity).getId();
    }

    @Override
    public long updatePermission(PermissionUpdateRequest request) {
        log.info("Update permission {}", request.getId());
        PermissionEntity entity = findById(request.getId());
        Set<RoleEntity> oldRoles = entity.getRoles();
        entity.setDescription(request.getDescription());
        entity.setPath(request.getPath());
        entity.setModule(request.getModule().toUpperCase());
        entity.setMethod(request.getMethod());
        long id = permissionRepository.save(entity).getId();
        invalidateCacheForRoles(oldRoles);
        invalidateCacheForRoles(entity.getRoles());
        log.info("Permission updated");
        return id;
    }

    @Override
    public long deletePermission(long id) {
        PermissionEntity entity = findById(id);
        log.info("Delete permission {}", entity.getModule());
        entity.setDeleted(true);
        permissionRepository.save(entity);
        invalidateCacheForRoles(entity.getRoles());
        log.info("Permission {} deleted successfully", entity.getModule());
        return entity.getId();
    }

    @Override
    public PermissionResponse getPermissionById(long id) {
        log.info("Get permission {}", id);
        PermissionEntity entity = findById(id);
        return convertToResponse(entity);
    }

    @Override
    public PermissionPageResponse getAllPermissions(int pageNo, int pageSize, boolean isDeleted) {
        int pageNoTemp = 0;
        if(pageNo > 0){
            pageNoTemp = pageNo -1;
        }
        //Pagging
        Pageable pageable = PageRequest.of(pageNoTemp, pageSize, Sort.by(Sort.Direction.ASC, "module", "createdAt"));
        Page<PermissionEntity> page = permissionRepository.findByIsDeleted(isDeleted, pageable);

        List<PermissionResponse> permissions = page.getContent().stream()
                .map(this::convertToResponse)
                .toList();
        return PermissionPageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .permissions(permissions)
                .build();
    }

    @Override
    public long restorePermission(long id) {
        PermissionEntity entity = findById(id);
        log.info("Restore permission {}", entity.getModule());
        entity.setDeleted(false);
        permissionRepository.save(entity);
        invalidateCacheForRoles(entity.getRoles());
        log.info("Permission {} restored successfully", entity.getModule());
        return entity.getId();
    }
    private PermissionEntity findById(long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("permission.not-found")));
    }
    private PermissionResponse convertToResponse(PermissionEntity entity) {
        return PermissionResponse.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .path(entity.getPath())
                .module(entity.getModule())
                .method(entity.getMethod())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
    private void invalidateCacheForRoles(Set<RoleEntity> roles) {
        if (roles != null) {
            roles.forEach(role -> {
                String cacheKey = CACHE_KEY_PREFIX + role.getName();
                redisTemplate.delete(cacheKey);
                log.debug("Invalidated cache for role: {}", role.getName());
            });
        }
    }
}
