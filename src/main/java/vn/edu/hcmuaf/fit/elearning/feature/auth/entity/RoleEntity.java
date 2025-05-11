package vn.edu.hcmuaf.fit.elearning.feature.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmuaf.fit.elearning.common.BaseEntity;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "tbl_role")
@Getter
@Setter
public class RoleEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 512)
    private String description;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted;

    @ManyToMany(fetch = FetchType.LAZY)
    Set<PermissionEntity> permissions = new HashSet<>();
}
