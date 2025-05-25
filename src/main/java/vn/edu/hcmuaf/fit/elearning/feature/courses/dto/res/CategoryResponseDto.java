package vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CategoryResponseDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String breadcrumb;
    private List<SubCategoryResponseDto> subCategories;
}

