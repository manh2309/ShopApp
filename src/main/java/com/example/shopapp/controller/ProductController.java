package com.example.shopapp.controller;

import com.example.shopapp.dtos.CategoriDTO;
import com.example.shopapp.dtos.ProductDTO;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    @GetMapping("")
    public ResponseEntity<?> getAllCategories(@RequestParam("page") int page,
                                              @RequestParam("limit") int limit){
        return  ResponseEntity.ok("This is products");
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") String productId){
        return  ResponseEntity.status(HttpStatus.OK).body("Co product id = "+ productId);
    }
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Nếu tham số truyền vào là 1 object thì sao? ==> Data Transfer Object = Request Object
    public ResponseEntity<?> createProduct(@Valid @ModelAttribute ProductDTO productDTO,
//                                              @RequestPart("file") MultipartFile file,
                                              BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            for(MultipartFile file : files){
                    if(file.getSize() == 0){
                        continue;
                    }
                    // Kiểm tra xem kích thước file có phù hợp không
                    if(file.getSize() > 10 * 1024 * 1024){ // Kích thước hơn 10MB
                        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File qua nang");
                    }
                    String contentType = file.getContentType();
                    if(contentType == null || !contentType.startsWith("image/")){
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Bat dau bang image");
                    }
                    //Lưu file và cập nhật thumbnail trong DTO
                    String filename = storeFile(file);
                    // Lưu đối tượng vào database ==> luu vao bang product_image
            }
            return  ResponseEntity.ok("Create product Successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // Them UUID vao truoc ten file de tao thanh ten file duy nhat
        String uniqueFileName = UUID.randomUUID().toString() + "_" + filename;
        // Duong dan den thu muc luu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        // Duong dan day du den file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        // Sao chép file vào thư mục chính
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        return  ResponseEntity.status(HttpStatus.OK).body("Xoa id = "+ id);
    }
}
