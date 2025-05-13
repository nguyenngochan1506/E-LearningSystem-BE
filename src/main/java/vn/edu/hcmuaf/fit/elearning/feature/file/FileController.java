package vn.edu.hcmuaf.fit.elearning.feature.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseDto uploadFile(@RequestParam() MultipartFile file) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("file.upload.success"))
                .data(fileService.saveFile(file))
                .build();
    }

    @GetMapping("/all")
    public ResponseDto getAllFiles(@RequestParam(required = false) String sort,
                                   @RequestParam(defaultValue = "0") int pageNo,
                                   @RequestParam(defaultValue = "10") int pageSize,
                                   @RequestParam(defaultValue = "false") boolean isDeleted) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("file.get.all.success"))
                .data(fileService.getAllFiles(sort, pageNo, pageSize, isDeleted))
                .build();
    }
    @DeleteMapping("/delete/{fileName}")
    public ResponseDto deleteFile(@PathVariable String fileName) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("file.delete.success"))
                .data(fileService.deleteFileByFileName(fileName))
                .build();
    }
}
