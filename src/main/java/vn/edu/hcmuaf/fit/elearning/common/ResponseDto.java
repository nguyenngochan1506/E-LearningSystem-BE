package vn.edu.hcmuaf.fit.elearning.common;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class ResponseDto implements Serializable {
    private int status;
    private String message;
    private Object data;
}
