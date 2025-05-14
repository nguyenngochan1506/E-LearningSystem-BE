package vn.edu.hcmuaf.fit.elearning.feature.file;

import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmuaf.fit.elearning.feature.file.dto.response.FilePageResponse;


public interface FileService {
    String saveFile(MultipartFile file);
    FilePageResponse getAllFiles(String sort, int pageNo, int pageSize, boolean isDeleted);
    FileEntity findById(Long id);
    FileEntity findByUrl(String url);

    Long deleteFileByFileName(String fileName);
    Long deleteFileByUrl(String url);
}
