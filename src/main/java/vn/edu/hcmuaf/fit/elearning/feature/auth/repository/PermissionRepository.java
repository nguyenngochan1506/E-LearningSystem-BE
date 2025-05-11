package vn.edu.hcmuaf.fit.elearning.feature.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.PermissionEntity;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    Page<PermissionEntity> findByIsDeleted(boolean isDeleted, Pageable pageable);

    @Query("SELECT p FROM PermissionEntity p JOIN p.roles r WHERE r.name IN :roleNames AND p.isDeleted = false")
    List<PermissionEntity> findByRoles_NameInAndIsDeletedFalse(List<String> roleNames);

    List<PermissionEntity> findByRoles_Name(String rolesName);
}
