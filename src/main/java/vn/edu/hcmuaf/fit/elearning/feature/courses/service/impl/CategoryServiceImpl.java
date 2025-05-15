package vn.edu.hcmuaf.fit.elearning.feature.courses.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateCateRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.CategoryResponseDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.SubCategoryResponseDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CategoryEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.repository.CategoryRepository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CategoryService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public CategoryEntity findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("category.not.found")));
    }

    @Override
    public Long createCate(CreateCateRequestDto req) {
        CategoryEntity category = new CategoryEntity();
        category.setName(req.getName());
        category.setDescription(req.getDescription());
        if(req.getParentId() != null) {
            CategoryEntity parentCategory = findById(req.getParentId());
            category.setCategory(parentCategory);
            String parentBreadcrumb = parentCategory.getBreadcrumb() != null
                    ? parentCategory.getBreadcrumb()
                    : parentCategory.getName();
            category.setBreadcrumb(parentBreadcrumb + " > " + req.getName());

        }else{
            category.setBreadcrumb(req.getName());
        }
        return categoryRepository.save(category).getId();
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        CategoryEntity category = findById(id);

        List<SubCategoryResponseDto> subCate = new ArrayList<>();
        collectSubCategories(category, subCate);
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setName(category.getName());
        categoryResponseDto.setDescription(category.getDescription());
        categoryResponseDto.setSubCategories(subCate);
        categoryResponseDto.setBreadcrumb(category.getBreadcrumb());

        return categoryResponseDto;
    }

    private void collectSubCategories(CategoryEntity category, List<SubCategoryResponseDto> subCate) {
        if (category.getSubCategories() != null && !category.getSubCategories().isEmpty()) {
            for (CategoryEntity subCategory : category.getSubCategories()) {
                SubCategoryResponseDto subCategoryResponseDto = new SubCategoryResponseDto();
                subCategoryResponseDto.setId(subCategory.getId());
                subCategoryResponseDto.setName(subCategory.getName());
                subCategoryResponseDto.setDescription(subCategory.getDescription());
                subCate.add(subCategoryResponseDto);
                collectSubCategories(subCategory, subCate);
            }
        }

    }
}
