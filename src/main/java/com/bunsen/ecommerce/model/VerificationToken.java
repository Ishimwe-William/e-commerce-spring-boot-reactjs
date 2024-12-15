package com.bunsen.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "verification_token")
@Data
@NoArgsConstructor
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Lob
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "created_timestamp", nullable = false)
    private Timestamp createdTimestamp;

    @ManyToOne
    @JoinColumn(name ="user_id", nullable = false)
    private AppUser user;
}
