package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.BaseResponse;
import vn.edu.hcmuaf.fit.elearning.feature.file.dto.response.FileResponseDto;

import java.util.List;

@SuperBuilder
@Getter
public class LessonResponseDto extends BaseResponse {
    private Long id;
    private String name;
    private String content;
    private Integer number;
    private Integer duration;
    private List<FileResponseDto> files;
    private String videoUrl;
}
