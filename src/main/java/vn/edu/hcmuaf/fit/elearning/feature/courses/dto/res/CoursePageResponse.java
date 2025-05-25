package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.PageResponse;

import java.util.List;

@Getter
@SuperBuilder
public class CoursePageResponse extends PageResponse {
    private List<CourseResponseDto> courses;
}