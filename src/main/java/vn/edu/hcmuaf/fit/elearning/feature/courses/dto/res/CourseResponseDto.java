package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.BaseResponse;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.res.UserResponse;

import java.util.List;

@Getter
@SuperBuilder
public class CourseResponseDto extends BaseResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String thumbnail;
    private CategoryResponseDto category;
    private UserResponse teacher;
    private Boolean isPublished;
    private List<ModuleResponseDto> modules;
}
