package ru.bmstu.my_apte4ka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.bmstu.my_apte4ka.entity.Item;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private List<Item> items;
    private Double totalCost;
}
