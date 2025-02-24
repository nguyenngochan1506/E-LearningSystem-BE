package vn.edu.ngochandev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.ngochandev.entities.UserEntity;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
