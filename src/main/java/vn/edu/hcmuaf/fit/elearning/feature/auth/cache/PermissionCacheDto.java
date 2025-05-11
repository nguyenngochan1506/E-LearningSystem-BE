package vn.edu.hcmuaf.fit.elearning.feature.auth.cache;

import lombok.*;
import vn.edu.hcmuaf.fit.elearning.common.enums.HttpMethod;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class PermissionCacheDto implements Serializable {
    private String path;
    private HttpMethod method;
    private String module;
    private String description;
}
