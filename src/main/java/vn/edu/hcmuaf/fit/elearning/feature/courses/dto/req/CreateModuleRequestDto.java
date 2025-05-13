package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class CreateModuleRequestDto implements Serializable {
    @NotBlank(message = "error.validate.not-blank")
    private String name;
    @NotNull(message = "error.validate.not-blank")
    private Integer number;
    private String description;
    @NotNull(message = "error.validate.not-blank")
    private Long courseId;
}
