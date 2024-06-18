package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "social_accounts")
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider", length = 20, nullable = false)
    private String provider;
    @Column(name = "provider_id", length = 50)
    private String providerId;
    @Column(name = "name", length = 150)
    private String name;
    @Column(name = "email", length = 150)
    private String email;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
