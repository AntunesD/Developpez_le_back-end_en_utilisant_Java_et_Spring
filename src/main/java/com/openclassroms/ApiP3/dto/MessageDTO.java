package com.openclassroms.ApiP3.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private Integer id;
    private Integer rentalId;
    private Integer userId;
    private String message;
}
