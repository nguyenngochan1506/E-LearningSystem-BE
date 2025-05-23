package vn.edu.hcmuaf.fit.elearning.feature.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import vn.edu.hcmuaf.fit.elearning.common.BaseEntity;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "tbl_role")
@Getter
@Setter
@NoArgsConstructor
public class RoleEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 512)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<PermissionEntity> permissions = new HashSet<>();
    public RoleEntity(String name, String description, Set<PermissionEntity> permissions) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;
    }
}
