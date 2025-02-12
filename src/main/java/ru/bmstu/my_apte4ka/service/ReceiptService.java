package ru.bmstu.my_apte4ka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bmstu.my_apte4ka.dto.CartDto;
import ru.bmstu.my_apte4ka.entity.Item;
import ru.bmstu.my_apte4ka.entity.Receipt;
import ru.bmstu.my_apte4ka.repository.ReceiptRepository;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    public Receipt makePurchase(CartDto cartDto) {
        Receipt receipt = Receipt.builder()
                .items(cartDto.getItems())
                .totalCost(cartDto.getTotalCost())
                .build();

        for (Item item : cartDto.getItems())
            item.setQuantity(item.getQuantity() - 1);

        return receiptRepository.save(receipt);
    }
}
