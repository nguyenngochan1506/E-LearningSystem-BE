package vn.edu.hcmuaf.fit.elearning.feature.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("select u " +
            "from UserEntity u " +
            "where u.isDeleted = :isDeleted and (lower(u.email)  like :keyword " +
            "    or lower(u.fullName)  like :keyword " +
            "    or lower(u.phoneNumber ) like :keyword )" )
    Page<UserEntity> searchByKeyword(@Param("keyword")String keyword,@Param("isDeleted") boolean isDeleted, Pageable pageable);
    Optional<UserEntity> findByEmail(String email);
    Page<UserEntity> findByIsDeleted(boolean isDeleted, Pageable pageable);
}
