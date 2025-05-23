package vn.edu.hcmuaf.fit.elearning.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.edu.hcmuaf.fit.elearning.common.enums.Gender;
import vn.edu.hcmuaf.fit.elearning.common.enums.HttpMethod;
import vn.edu.hcmuaf.fit.elearning.common.enums.UserStatus;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.PermissionEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.RoleEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.repository.RoleRepository;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserEntity;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedingData implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        long count = userRepository.count();
        if(count == 0){
            List<PermissionEntity> permissionEntityList = List.of(
                    // Lesson API
                    new PermissionEntity(HttpMethod.PUT, "/api/v1/lessons", "Update all lessons", "Lesson"),
                    new PermissionEntity(HttpMethod.POST, "/api/v1/lessons", "Create a lesson", "Lesson"),
                    new PermissionEntity(HttpMethod.PATCH, "/api/v1/lessons/restore/:id", "Restore a lesson by id", "Lesson"),
                    new PermissionEntity(HttpMethod.PATCH, "/api/v1/lessons/remove-file", "Remove a file from lesson", "Lesson"),
                    new PermissionEntity(HttpMethod.PATCH, "/api/v1/lessons/add-file", "Add a file to lesson", "Lesson"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/lessons/:id", "Get a lesson by id", "Lesson"),
                    new PermissionEntity(HttpMethod.DELETE, "/api/v1/lessons/:id", "Delete a lesson by id", "Lesson"),
                    new PermissionEntity(HttpMethod.DELETE, "/api/v1/lessons/permanently/:id", "Permanently delete a lesson by id", "Lesson"),

                    // Module API
                    new PermissionEntity(HttpMethod.PUT, "/api/v1/modules", "Update all modules", "Module"),
                    new PermissionEntity(HttpMethod.POST, "/api/v1/modules", "Create a module", "Module"),
                    new PermissionEntity(HttpMethod.PATCH, "/api/v1/modules/restore/:id", "Restore a module by id", "Module"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/modules/:id", "Get a module by id", "Module"),
                    new PermissionEntity(HttpMethod.DELETE, "/api/v1/modules/:id", "Delete a module by id", "Module"),
                    new PermissionEntity(HttpMethod.DELETE, "/api/v1/modules/permanently/:id", "Permanently delete a module by id", "Module"),

                    // File API
                    new PermissionEntity(HttpMethod.POST, "/api/v1/files/upload", "Upload a file", "File"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/files/all", "Get all files", "File"),
                    new PermissionEntity(HttpMethod.DELETE, "/api/v1/files/delete/:fileName", "Delete a file by filename", "File"),

                    // Permission API
                    new PermissionEntity(HttpMethod.GET, "/api/v1/permissions", "Get all permissions", "Permission"),
                    new PermissionEntity(HttpMethod.PUT, "/api/v1/permissions", "Update all permissions", "Permission"),
                    new PermissionEntity(HttpMethod.POST, "/api/v1/permissions", "Create a permission", "Permission"),
                    new PermissionEntity(HttpMethod.PATCH, "/api/v1/permissions/restore/:id", "Restore a permission by id", "Permission"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/permissions/:id", "Get a permission by id", "Permission"),
                    new PermissionEntity(HttpMethod.DELETE, "/api/v1/permissions/:id", "Delete a permission by id", "Permission"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/permissions/trash", "Get all trashed permissions", "Permission"),

                    // User API
                    new PermissionEntity(HttpMethod.PUT, "/api/v1/users/update", "Update user information", "User"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/users", "Get all users", "User"),
                    new PermissionEntity(HttpMethod.POST, "/api/v1/users", "Create a user", "User"),
                    new PermissionEntity(HttpMethod.PATCH, "/api/v1/users/restore/:id", "Restore a user by id", "User"),
                    new PermissionEntity(HttpMethod.PATCH, "/api/v1/users/change-pwd", "Change user password", "User"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/users/:id", "Get a user by id", "User"),
                    new PermissionEntity(HttpMethod.DELETE, "/api/v1/users/:id", "Delete a user by id", "User"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/users/trash", "Get all trashed users", "User"),
                    new PermissionEntity(HttpMethod.GET, "/api/auth/me", "Get my profile", "AUTH"),

                    // Course API
                    new PermissionEntity(HttpMethod.PUT, "/api/v1/courses", "Update all courses", "Course"),
                    new PermissionEntity(HttpMethod.POST, "/api/v1/courses", "Create a course", "Course"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/courses/:id", "Get a course by id", "Course"),

                    // Role API
                    new PermissionEntity(HttpMethod.GET, "/api/v1/roles", "Get all roles", "Role"),
                    new PermissionEntity(HttpMethod.PUT, "/api/v1/roles", "Update all roles", "Role"),
                    new PermissionEntity(HttpMethod.POST, "/api/v1/roles", "Create a role", "Role"),
                    new PermissionEntity(HttpMethod.POST, "/api/v1/roles/assign-user", "Assign a role to a user", "Role"),
                    new PermissionEntity(HttpMethod.POST, "/api/v1/roles/assign-permission", "Assign a permission to a role", "Role"),
                    new PermissionEntity(HttpMethod.PATCH, "/api/v1/roles/restore/:id", "Restore a role by id", "Role"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/roles/:id", "Get a role by id", "Role"),
                    new PermissionEntity(HttpMethod.DELETE, "/api/v1/roles/:id", "Delete a role by id", "Role"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/roles/trash", "Get all trashed roles", "Role"),

                    // Category Controller
                    new PermissionEntity(HttpMethod.POST, "/api/v1/categories", "Create a category", "Category"),
                    new PermissionEntity(HttpMethod.GET, "/api/v1/categories/:id", "Get a category by id", "Category")
            );

            RoleEntity roleEntity = new RoleEntity("admin", "admin hehe", new HashSet<>(permissionEntityList));

            UserEntity userEntity = new UserEntity();
            userEntity.setEmail("admin@gmail.com");
            userEntity.setPassword(passwordEncoder.encode("123"));
            userEntity.setFullName("admin");
            userEntity.setPhoneNumber("0123456789");
            userEntity.setGender(Gender.FEMALE);
            userEntity.setDateOfBirth(LocalDate.of(2004, 1,1));
            userEntity.setAvatar("");
            userEntity.setStatus(UserStatus.ACTIVE);
            userEntity.setRoles(new HashSet<>(List.of(roleEntity)));

            userRepository.save(userEntity);
        }

    }
}
