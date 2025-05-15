package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CreateCourseRequestDto implements Serializable {
    @NotBlank(message = "error.validate.not-blank")
    private String name;
    @NotBlank(message = "error.validate.not-blank")
    private String description;
    @NotNull(message = "error.validate.not-blank")
    private Double price;
    @NotNull(message = "error.validate.not-blank")
    private Boolean isPublished;
    @NotNull(message = "error.validate.not-blank")
    private MultipartFile thumbnail;
    @NotNull(message = "error.validate.not-blank")
    private Long categoryId;
}
