package vn.edu.hcmuaf.fit.elearning.feature.file.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class FileResponseDto implements Serializable {
    private Long id;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long size;
}
