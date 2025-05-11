package vn.edu.hcmuaf.fit.elearning.feature.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmuaf.fit.elearning.common.BaseEntity;
import vn.edu.hcmuaf.fit.elearning.common.enums.Gender;
import vn.edu.hcmuaf.fit.elearning.common.enums.UserStatus;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.RoleEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;

    @Column(name = "phone_number", length = 15, unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name ="password", length = 255, nullable = false)
    private String password;

    @Column(name ="status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "avatar", length = 512)
    private String avatar;

    @Column(name = "is_deleted", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<RoleEntity> roles = new HashSet<>();
}
