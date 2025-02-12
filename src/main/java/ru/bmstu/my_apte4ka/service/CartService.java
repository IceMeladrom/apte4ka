package ru.bmstu.my_apte4ka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.bmstu.my_apte4ka.dto.CartDto;
import ru.bmstu.my_apte4ka.entity.Item;
import ru.bmstu.my_apte4ka.entity.Receipt;
import ru.bmstu.my_apte4ka.exception.EmptyCartException;
import ru.bmstu.my_apte4ka.repository.ItemRepository;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final ReceiptService receiptService;

    public CartDto getItemsAsList(String cartCookie) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String decodedCookie = URLDecoder.decode(cartCookie, StandardCharsets.UTF_8);
        List<Long> cart = objectMapper.readValue(decodedCookie, new TypeReference<List<Long>>() {
        });

        List<Item> items = new ArrayList<>();
        Double totalCost = (double) 0;
        for (Long itemId : cart) {
            try {
                Item item = itemService.get(itemId);
                items.add(item);
                totalCost += item.getPrice();
            } catch (EntityNotFoundException ignored) {
            }

        }

        return new CartDto(items, totalCost);
    }

    public boolean addItem(Long itemId, String cartCookie,
                           HttpServletResponse response)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String decodedCookie = URLDecoder.decode(cartCookie, StandardCharsets.UTF_8);
        List<Long> cart = objectMapper.readValue(decodedCookie, new TypeReference<List<Long>>() {
        });

        long cnt = cart.stream().filter(e -> e.equals(itemId)).count();

        if (cnt >= itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new).getQuantity())
            return false;

        cart.add(itemId);


        String updatedCart = objectMapper.writeValueAsString(cart);
        String encodedCart = URLEncoder.encode(updatedCart, StandardCharsets.UTF_8);
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("cart", encodedCart);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);
        return true;
    }

    public void deleteItem(Long productId, String cartCookie, HttpServletResponse response)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String decodedCookie = URLDecoder.decode(cartCookie, StandardCharsets.UTF_8);
        List<Long> cart = objectMapper.readValue(decodedCookie, new TypeReference<List<Long>>() {
        });

        cart.remove(productId);

        String updatedCart = objectMapper.writeValueAsString(cart);
        String encodedCart = URLEncoder.encode(updatedCart, StandardCharsets.UTF_8);
        Cookie cookie = new Cookie("cart", encodedCart);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(24 * 60 * 60); // Cookie действует 1 день

        response.addCookie(cookie);
    }

    public Receipt makePurchaseThenClearCart(String cartCookie, HttpServletResponse response)
            throws JsonProcessingException, EmptyCartException {
        ObjectMapper objectMapper = new ObjectMapper();

        String decodedCookie = URLDecoder.decode(cartCookie, StandardCharsets.UTF_8);
        List<Long> cart = objectMapper.readValue(decodedCookie, new TypeReference<List<Long>>() {
        });


        CartDto cartDto = new CartDto(new ArrayList<>(), (double) 0);

        for (Long itemId : cart) {
            try {
                Item item = itemService.get(itemId);
                cartDto.getItems().add(item);
                cartDto.setTotalCost(Double.sum(cartDto.getTotalCost(), item.getPrice()));
            } catch (EntityNotFoundException ignored) {
                throw new EntityNotFoundException("The item in the cart was not found in the database");
            }

        }

        if (cartDto.getItems().isEmpty())
            throw new EmptyCartException("Корзина пуста");

        Receipt receipt = receiptService.makePurchase(cartDto);
        clearCart(response);
        return receipt;

    }

    public void clearCart(HttpServletResponse response) {
        Cookie cookie = new Cookie("cart", "[]");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
