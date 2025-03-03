package vn.edu.ngochandev.feature.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("select u " +
            "from UserEntity u " +
            "where lower(u.className) like :keyword " +
            "    or lower(u.email)  like :keyword " +
            "    or lower(u.fullName)  like :keyword " +
            "    or lower(u.phoneNumber ) like :keyword " +
            "    or lower(u.userName)  like :keyword ")
    Page<UserEntity> searchByKeyword(String keyword, Pageable pageable);
}
