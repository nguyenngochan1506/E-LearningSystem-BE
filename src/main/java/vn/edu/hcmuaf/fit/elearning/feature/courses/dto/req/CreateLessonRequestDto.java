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
public class CreateLessonRequestDto implements Serializable {
    @NotBlank(message = "error.validate.not-blank")
    private String name;
    @NotBlank(message = "error.validate.not-blank")
    private String content;
    @NotNull(message = "error.validate.not-blank")
    private Integer number;
    @NotNull(message = "error.validate.not-blank")
    private Long moduleId;
    private String videoUrl;
    private MultipartFile videoFile;
    private List<MultipartFile> files;

    @AssertTrue(message = "lesson.create.video.url.or.file")
    public boolean isVideoUrlOrFile() {
        return (videoUrl != null && !videoUrl.isBlank()) || (videoFile != null && !videoFile.isEmpty());
    }
}
