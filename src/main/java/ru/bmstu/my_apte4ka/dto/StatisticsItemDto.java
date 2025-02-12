package ru.bmstu.my_apte4ka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.bmstu.my_apte4ka.entity.Item;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsItemDto {
    private Item item;
    private Long amount;
}
