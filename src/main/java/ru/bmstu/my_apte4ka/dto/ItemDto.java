package ru.bmstu.my_apte4ka.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ItemDto {
    private String name;
    private Double price;
    private Integer quantity = 0;
}
