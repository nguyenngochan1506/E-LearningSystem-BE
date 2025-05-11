package vn.edu.hcmuaf.fit.elearning.feature.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmuaf.fit.elearning.common.BaseEntity;
import vn.edu.hcmuaf.fit.elearning.common.enums.HttpMethod;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "tbl_permission")
@Getter
@Setter
public class PermissionEntity extends BaseEntity {
    @Column(name = "method", nullable = false)
    @Enumerated(EnumType.STRING)
    private HttpMethod method;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;

    @Column(name = "module", length = 64)
    private String module;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<RoleEntity> roles = new HashSet<>();
}
