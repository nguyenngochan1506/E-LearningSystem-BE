package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res;

import java.io.Serializable;

public class CreateCourseRequestDto implements Serializable {
    private String name;
    private String description;
    private Double price;
    private Boolean isPublished;
}
