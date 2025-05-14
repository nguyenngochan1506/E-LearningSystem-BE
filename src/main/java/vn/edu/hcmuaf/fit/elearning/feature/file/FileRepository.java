package vn.edu.hcmuaf.fit.elearning.feature.file;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Page<FileEntity> findByIsDeleted(boolean isDeleted, Pageable pageable);

    FileEntity findByName(String name);

    FileEntity findByUrl(String url);
}
