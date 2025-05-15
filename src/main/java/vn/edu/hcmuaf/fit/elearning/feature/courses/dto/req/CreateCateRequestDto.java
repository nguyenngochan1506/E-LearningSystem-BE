package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class CreateCateRequestDto implements Serializable {
    @NotBlank(message = "error.validate.not-blank")
    private String name;
    @NotBlank(message = "error.validate.not-blank")
    private String description;
    private Long parentId;
}
