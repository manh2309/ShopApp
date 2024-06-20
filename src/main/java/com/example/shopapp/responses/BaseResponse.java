package com.example.shopapp.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseResponse {
    @JsonProperty("create_at")
    private LocalDateTime createdAt;
    @JsonProperty("update_at")
    private LocalDateTime updatedAt;
}
