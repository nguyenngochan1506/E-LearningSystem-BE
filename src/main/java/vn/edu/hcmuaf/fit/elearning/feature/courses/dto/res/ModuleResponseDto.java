package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.BaseResponse;

import java.util.List;

@SuperBuilder
@Getter
public class ModuleResponseDto extends BaseResponse {
    private Long id;
    private String name;
    private String description;
    private Integer number;
    private List<LessonResponseDto> lessons;
}
