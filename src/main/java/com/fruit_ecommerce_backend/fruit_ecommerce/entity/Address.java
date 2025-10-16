package com.fruit_ecommerce_backend.fruit_ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Street details like house no, building name, etc.
    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String country;

    // Optional landmark for better delivery location identification
    private String landmark;

    // Phone number specific to delivery address
    @Column(length = 15)
    private String phoneNumber;

    // Address type like HOME, WORK, etc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType addressType;

    // Mark if this is the default address for the user
    @Column(nullable = false)
    private boolean isDefault = false;

    /**
     * Mapping to the User entity.
     * Many addresses can belong to one user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
