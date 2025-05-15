package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UpdateModuleRequestDto implements Serializable {
    @NotNull(message = "error.validate.not-blank")
    private Long id;
    private String name;
    private Integer number;
    private String description;
}
