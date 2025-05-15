package vn.edu.hcmuaf.fit.elearning.feature.courses.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateCateRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CategoryService;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("{id}")
    public ResponseDto findById(@PathVariable Long id){
        return ResponseDto.builder()
                .status(200)
                .message(Translator.translate("category.get.success"))
                .data(categoryService.getById(id))
                .build();
    }
    @PostMapping()
    public ResponseDto createCategory(@RequestBody @Valid CreateCateRequestDto req){
        return ResponseDto.builder()
                .status(200)
                .message(Translator.translate("category.create.success"))
                .data(categoryService.createCate(req))
                .build();
    }
}
