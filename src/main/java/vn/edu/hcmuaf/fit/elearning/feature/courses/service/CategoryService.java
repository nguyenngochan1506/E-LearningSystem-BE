package vn.edu.hcmuaf.fit.elearning.feature.courses.service;

import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateCateRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.CategoryResponseDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CategoryEntity;

public interface CategoryService{
    CategoryEntity findById(Long id);

    Long createCate(CreateCateRequestDto req);

    CategoryResponseDto getById(Long id);
}
