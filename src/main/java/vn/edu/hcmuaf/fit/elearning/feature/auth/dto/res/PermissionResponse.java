package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.BaseResponse;
import vn.edu.hcmuaf.fit.elearning.common.enums.HttpMethod;


@Getter
@SuperBuilder
public class PermissionResponse extends BaseResponse {
    private Long id;
    private HttpMethod method;
    private String path;
    private String description;
    private String module;
}
