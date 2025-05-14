package vn.edu.hcmuaf.fit.elearning.feature.file.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import vn.edu.hcmuaf.fit.elearning.feature.file.FileEntity;
import vn.edu.hcmuaf.fit.elearning.feature.file.FileRepository;
import vn.edu.hcmuaf.fit.elearning.feature.file.FileService;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import vn.edu.hcmuaf.fit.elearning.feature.file.dto.response.FilePageResponse;
import vn.edu.hcmuaf.fit.elearning.feature.file.dto.response.FileResponseDto;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final S3Client s3Client;
    @Value("${aws.s3.bucketName}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

    @Override
    public String saveFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String fileUrl = saveToS3Bucket(file, fileName);
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(fileName);
        fileEntity.setUrl(fileUrl);
        fileEntity.setSize(file.getSize());
        fileEntity.setType(file.getContentType());
        fileEntity.setDeleted(false);
        fileRepository.save(fileEntity);
        return fileUrl;
    }


    @Override
    public FilePageResponse getAllFiles(String sort, int pageNo, int pageSize, boolean isDeleted) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        //Sort
        if(StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");//columName:asc|desc
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                String column = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    order = new Sort.Order(Sort.Direction.ASC, column);
                }else {
                    order = new Sort.Order(Sort.Direction.DESC, column);
                }
            }
        }
        int pageNoTemp = 0;
        if(pageNo > 0){
            pageNoTemp = pageNo -1;
        }

        //Pagging
        Pageable pageable = PageRequest.of(pageNoTemp, pageSize, Sort.by(order));
        Page<FileEntity> fileEntityPage = fileRepository.findByIsDeleted(isDeleted, pageable);

        List<FileResponseDto> files = fileEntityPage.getContent().stream()
                .map(fileEntity -> FileResponseDto.builder()
                        .id(fileEntity.getId())
                        .fileName(fileEntity.getName())
                        .fileUrl(fileEntity.getUrl())
                        .size(fileEntity.getSize())
                        .fileType(fileEntity.getType())
                        .build())
                .toList();

        return FilePageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(fileEntityPage.getTotalPages())
                .totalElements(fileEntityPage.getTotalElements())
                .files(files)
                .build();
    }

    @Override
    public FileEntity findById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    @Override
    public FileEntity findByUrl(String url) {
        return fileRepository.findByUrl(url);
    }

    @Override
    public Long deleteFileByFileName(String fileName) {
        // Tìm tệp trong cơ sở dữ liệu
        FileEntity fileEntity = fileRepository.findByName(fileName);
        if (fileEntity != null) {
            // Đánh dấu tệp là đã xóa trong cơ sở dữ liệu
            fileRepository.delete(fileEntity);

            // Xóa tệp từ S3
            deleteFileFromS3Bucket(fileName);
            return fileEntity.getId();
        }
        return 0L;
    }

    @Override
    public Long deleteFileByUrl(String url) {
        // Tìm tệp trong cơ sở dữ liệu
        FileEntity fileEntity = fileRepository.findByUrl(url);
        if (fileEntity != null) {
            fileRepository.delete(fileEntity);
            // Xóa tệp từ S3
            deleteFileFromS3Bucket(fileEntity.getName());
            return fileEntity.getId();
        }
        return null;
    }

    private void deleteFileFromS3Bucket(String fileName) {
        try {
            // Xóa tệp từ S3
            s3Client.deleteObject(builder -> builder.bucket(bucketName).key(fileName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String saveToS3Bucket(MultipartFile file, String fileName) {
        try {
            // Upload the file to S3
            InputStream inputStream = file.getInputStream();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, file.getSize()));

            // Tạo URL cho tệp đã tải lên
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
