package vn.edu.ngochandev.feature.user;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.edu.ngochandev.common.Gender;
import vn.edu.ngochandev.common.UserStatus;
import vn.edu.ngochandev.common.UserType;
import vn.edu.ngochandev.common.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    @Column(name = "class_name", length = 50)
    private String className;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private Gender gender;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;

    @Column(name = "phone_number", length = 15, unique = true)
    private String phoneNumber;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name = "username", unique = true, length = 255)
    private String userName;

    @Column(name ="password", length = 255)
    private String password;

    @Column(name ="status")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private UserStatus status;

    @Column(name ="user_type")
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private UserType userType;

    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastLogin;
}
