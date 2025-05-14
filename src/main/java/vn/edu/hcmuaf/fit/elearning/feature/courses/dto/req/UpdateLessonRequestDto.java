package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UpdateLessonRequestDto implements Serializable {
    @NotNull(message = "error.validate.not-blank")
    private Long id;
    private String name;
    private String content;
    private Integer number;
    private Integer duration;
    private String videoUrl;
    private MultipartFile videoFile;
}
