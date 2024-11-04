package com.openclassroms.ApiP3.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private Integer id;
    private Integer rental_id;
    private Integer user_id;
    private String message;
}
