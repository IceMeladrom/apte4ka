package ru.bmstu.my_apte4ka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.bmstu.my_apte4ka.dto.StatisticsItemDto;
import ru.bmstu.my_apte4ka.entity.Item;
import ru.bmstu.my_apte4ka.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final ItemRepository itemRepository;

    public StatisticsItemDto getTopSellingItem() {
        List<StatisticsItemDto> items = itemRepository.findTopSellingItems(PageRequest.of(0, 1));
        return items.isEmpty() ? null : items.getFirst();
    }

    public StatisticsItemDto getWorstSellingItem() {
        List<StatisticsItemDto> worstItems = itemRepository.findWorstSellingItems(PageRequest.of(0, 1));
        List<StatisticsItemDto> topItems = itemRepository.findTopSellingItems(PageRequest.of(0, 1));
        if (topItems.equals(worstItems))
            return null;
        else
            return worstItems.isEmpty() ? null : worstItems.getFirst();
    }

    public Item getTheMostExpensiveItem() {
        return itemRepository.findFirstByOrderByPriceDesc().orElse(null);
    }

    public Item getTheMostCheapestItem() {
        return itemRepository.findFirstByOrderByPriceAsc().orElse(null);
    }

    public Item getTheLargestQuantityOfItems() {
        return itemRepository.findFirstByOrderByQuantityDesc().orElse(null);
    }

    public Item getTheSmallestQuantityOfItems() {
        return itemRepository.findFirstByOrderByQuantityAsc().orElse(null);
    }


}
