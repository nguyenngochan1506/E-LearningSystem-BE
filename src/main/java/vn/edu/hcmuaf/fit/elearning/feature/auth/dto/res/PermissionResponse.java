package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res;

import lombok.Builder;
import lombok.Getter;
import vn.edu.hcmuaf.fit.elearning.common.enums.HttpMethod;

import java.io.Serializable;

@Getter
@Builder
public class PermissionResponse implements Serializable {
    private Long id;
    private HttpMethod method;
    private String path;
    private String description;
}
