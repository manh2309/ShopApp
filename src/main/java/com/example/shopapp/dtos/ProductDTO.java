package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    @NotEmpty(message = "Khong de trong")
    @Size(min = 3, max = 200, message = "cos 3 ky tro len")
    private String name;
    @Min(value = 0, message = "Khong nho hon 0")
    @Max(value = 10000000, message = "Khong lon hon 10tr")
    private Float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private String categoryId;
    private List<MultipartFile> files;
}
