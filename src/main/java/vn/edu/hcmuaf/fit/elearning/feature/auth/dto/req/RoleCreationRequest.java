package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class RoleCreationRequest implements Serializable {
    @NotBlank(message = "error.validate.not-blank")
    private  String name;

    @NotBlank(message = "error.validate.not-blank")
    private  String description;
}
