package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name" , nullable = false, length = 350)
    private String name;
    private Float price;
    @Column(name = "thumbnail", length = 300)
    private String thumbNail;
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
