package vn.edu.hcmuaf.fit.elearning.feature.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.auth.cache.PermissionCacheDto;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.AssignRoleToPermissionRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.RoleCreationRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.RoleUpdateRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.PermissionResponse;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.RolePageResponse;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.RoleResponse;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.PermissionEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.RoleEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.repository.PermissionRepository;
import vn.edu.hcmuaf.fit.elearning.feature.auth.repository.RoleRepository;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY_PREFIX = "permissions:";

    @Override
    public long createRole(RoleCreationRequest request) {
        RoleEntity role = new RoleEntity();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setDeleted(false);

        //save to db
        return roleRepository.save(role).getId();
    }

    @Override
    public long updateRole(RoleUpdateRequest request) {
        //find role by id
        RoleEntity role = getRoleEntityById(request.getId());
        //update role
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        //save to db
        roleRepository.save(role);
        invalidateCacheForRole(role.getName());
        return role.getId();
    }

    @Override
    public long deleteRole(long id) {
        //find role by id
        RoleEntity role = getRoleEntityById(id);
        //update role
        role.setDeleted(true);
        //save to db
        roleRepository.save(role);
        invalidateCacheForRole(role.getName());
        return id;
    }

    @Override
    public RoleResponse getRoleById(long id) {
        RoleEntity role = getRoleEntityById(id);

        return convertToResponse(role);
    }

    @Override
    public RolePageResponse getAllRoles(int pageNo, int pageSize, boolean isDeleted) {
        int pageNoTemp = 0;
        if(pageNo > 0){
            pageNoTemp = pageNo -1;
        }

        //Pagging
        Pageable pageable = PageRequest.of(pageNoTemp, pageSize);
        Page<RoleEntity> roleEntities = roleRepository.findAllByIsDeleted(isDeleted, pageable);
        List<RoleResponse> responses = roleEntities.getContent().stream().map(this::convertToResponse).toList();
        return RolePageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(roleEntities.getTotalPages())
                .totalElements(roleEntities.getTotalElements())
                .roles(responses)
                .build();
    }

    @Override
    public long restoreRole(long id) {
        //find role by id
        RoleEntity role = getRoleEntityById(id);
        //update role
        role.setDeleted(false);
        //save to db
        roleRepository.save(role);
        invalidateCacheForRole(role.getName());
        return id;
    }

    @Override
    public long assignRolePermission(AssignRoleToPermissionRequest req) {
        //check
        if(req.getPermissionIds() == null ){
            throw new IllegalArgumentException(Translator.translate("role.permission.empty"));
        }
        // get role by id
        RoleEntity role = getRoleEntityById(req.getRoleId());
        // set permission is empty
        role.setPermissions(Set.of());

        // set permission
        Set<PermissionEntity>  pers = new HashSet<>();
        for(Long permissionId : req.getPermissionIds()){
            PermissionEntity permission = permissionRepository.findById(permissionId).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("permission.not-found")));
            pers.add(permission);
        }
        role.setPermissions(pers);
        // save to db
        roleRepository.save(role);
        invalidateCacheForRole(role.getName());
        return role.getId();
    }

    private RoleEntity getRoleEntityById(long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("role.not-found")));
    }
    private RoleResponse convertToResponse(RoleEntity role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(role.getPermissions().stream().map(p ->(PermissionResponse) PermissionResponse.builder()
                        .id(p.getId())
                        .method(p.getMethod())
                        .path(p.getPath())
                        .description(p.getDescription())
                        .createdBy(p.getCreatedBy())
                        .createdAt(p.getCreatedAt())
                        .updatedBy(p.getUpdatedBy())
                        .updatedAt(p.getUpdatedAt())
                        .build()).toList())
                .build();
    }
    private void invalidateCacheForRole(String roleName) {
        String cacheKey = CACHE_KEY_PREFIX + roleName;
        redisTemplate.delete(cacheKey);
        log.debug("Invalidated cache for role: {}", roleName);
    }
}
