package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
public class UpdateCourseRequestDto implements Serializable {
    @NotNull(message = "error.validate.not-blank")
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Boolean isPublished;
    private Long categoryId;
    private MultipartFile thumbnail;
}
