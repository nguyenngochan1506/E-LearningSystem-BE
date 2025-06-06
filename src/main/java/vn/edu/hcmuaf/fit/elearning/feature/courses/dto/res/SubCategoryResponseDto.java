package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SubCategoryResponseDto  implements Serializable {
    private Long id;
    private String name;
    private String description;
}
