package vn.edu.hcmuaf.fit.elearning.common;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@SuperBuilder
@Getter
public class BaseResponse implements Serializable {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
