package com.fruit_ecommerce_backend.fruit_ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;

    private double price;
    private int stock;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] image;

    private String category;

    private Double offerPercentage;
    private LocalDate offerStartDate;
    private LocalDate offerEndDate;

    private Boolean festivalOffer;
    private String festivalName;

    // Persisted column name in DB is offer_price
    @Column(name = "offer_price")
    private Double offerPrice;

    /** Compute and set offerPrice based on offerPercentage and dates.
     * If there is an offer percentage and dates cover today, set discounted price.
     * Otherwise set offerPrice = price as fallback (so column always contains effective price).
     */
    public void computeAndSetOfferPrice() {
        if (this.offerPercentage != null) {
            // compute discount irrespective of dates if you want stored value as "after-offer value".
            // But to keep behavior consistent with isOfferActive, we will compute discounted value when percentage exists.
            double discount = (this.price * this.offerPercentage / 100.0);
            this.offerPrice = roundTwoDecimals(this.price - discount);
            return;
        }
        // fallback -> store actual price
        this.offerPrice = roundTwoDecimals(this.price);
    }

    @Transient
    public boolean isOfferActive() {
        if (this.offerPercentage != null && this.offerStartDate != null && this.offerEndDate != null) {
            LocalDate today = LocalDate.now();
            return !today.isBefore(this.offerStartDate) && !today.isAfter(this.offerEndDate);
        }
        return false;
    }

    private Double roundTwoDecimals(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}
