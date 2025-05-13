package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
public class UpdateCourseRequestDto extends CreateCourseRequestDto {
    @NotNull(message = "error.validate.not-blank")
    private Long id;
}
