package vn.edu.hcmuaf.fit.elearning.feature.file;

import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmuaf.fit.elearning.feature.file.dto.response.FilePageResponse;

import java.util.List;

public interface FileService {
    String saveFile(MultipartFile file);
    FilePageResponse getAllFiles(String sort, int pageNo, int pageSize, boolean isDeleted);

    Long deleteFile(String fileName);
}
