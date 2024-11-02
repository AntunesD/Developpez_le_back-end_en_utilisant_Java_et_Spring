package com.openclassroms.ApiP3.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RentalDTO {
    private Integer id;
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private byte[] picture;
    private String description;
    private Integer ownerId;
    private String ownerName;
    private String created_at;
    private String updated_at;
}
