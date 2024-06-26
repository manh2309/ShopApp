package com.example.shopapp.controller;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.ProductListResponse;
import com.example.shopapp.responses.ProductResponse;
import com.example.shopapp.servies.IProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    @Autowired
    private IProductService iProductService;
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(@RequestParam("page") int page,
                                                           @RequestParam("limit") int limit){
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = iProductService.getAllProducts(pageRequest);
        // Lấy tổng số trang
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return  ResponseEntity.ok(ProductListResponse.builder().products(products).totalPages(totalPages).build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") String productId){
        return  ResponseEntity.status(HttpStatus.OK).body("Co product id = "+ productId);
    }
    @PostMapping(value = "")
    // Nếu tham số truyền vào là 1 object thì sao? ==> Data Transfer Object = Request Object
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                           BindingResult result){
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = iProductService.createProduct(productDTO);
            return  ResponseEntity.ok(newProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files){
        try {
            Product exitstingProduct = iProductService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("You can only uploads maximum 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
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
                ProductImage newProductImage = iProductService.createProductImage(
                        exitstingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build());
                productImages.add(newProductImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private String storeFile(MultipartFile file) throws IOException {
        if(!isImageFile(file) || file.getOriginalFilename() == null){
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
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

        private  boolean isImageFile(MultipartFile file){
            String contentType = file.getContentType();
            return contentType != null && contentType.startsWith("image/");
        }
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteProduct(@PathVariable Long id){
            return  ResponseEntity.status(HttpStatus.OK).body("Xoa id = "+ id);
        }
        @PostMapping("/generateFakeProducts")
        public ResponseEntity<?> generateFakeProducts(){
            Faker faker = new Faker();
            for(int i = 0; i < 1_000_000; i++){
                String productName = faker.commerce().productName();
                if(iProductService.existsByName(productName)){
                    continue;
                }
                ProductDTO productDTO = ProductDTO.builder()
                        .name(productName)
                        .price((float)faker.number().numberBetween(10, 90_000_000))
                        .thumbnail("")
                        .description(faker.lorem().sentence())
                        .categoryId((long)faker.number().numberBetween(1,4))
                        .build();
                try {
                    iProductService.createProduct(productDTO);
                } catch (DataNotFoundException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }
            return ResponseEntity.ok("Fake products created Successfully!");
        }
    }