package vn.edu.ngochandev.feature.user.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.edu.ngochandev.common.PageResponse;

import java.util.List;

@Getter
@SuperBuilder
public class UserPageResponse extends PageResponse{
    List<UserResponse> users;
}
