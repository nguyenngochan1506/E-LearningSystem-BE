package vn.edu.hcmuaf.fit.elearning.feature.file.dto.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.PageResponse;

import java.util.List;

@SuperBuilder
@Getter
public class FilePageResponse extends PageResponse {
    private List<FileResponseDto> files;
}
