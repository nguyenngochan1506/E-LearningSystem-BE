package vn.edu.hcmuaf.fit.elearning.feature.user.dto.res;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.PageResponse;

import java.util.List;

@Getter
@SuperBuilder
public class UserPageResponse extends PageResponse {
    List<UserResponse> users;
}
