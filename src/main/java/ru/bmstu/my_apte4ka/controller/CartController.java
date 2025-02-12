package ru.bmstu.my_apte4ka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.bmstu.my_apte4ka.dto.CartDto;
import ru.bmstu.my_apte4ka.entity.Receipt;
import ru.bmstu.my_apte4ka.exception.EmptyCartException;
import ru.bmstu.my_apte4ka.service.CartService;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping()
    public String getCartPage(@CookieValue(value = "cart", defaultValue = "[]") String cartCookie, Model model)
            throws JsonProcessingException {

        CartDto cartDto = cartService.getItemsAsList(cartCookie);

        model.addAttribute("cart", cartDto.getItems());
        model.addAttribute("totalCost", cartDto.getTotalCost());
        return "cart";
    }


    @GetMapping("/add/item/{itemId}")
    public String addItem(@PathVariable Long itemId,
                          @CookieValue(value = "cart", defaultValue = "[]") String cartCookie,
                          HttpServletResponse response, RedirectAttributes redirectAttributes)
            throws JsonProcessingException {

        boolean isAdded = cartService.addItem(itemId, cartCookie, response);

        redirectAttributes.addFlashAttribute("isItemAddedToCart", isAdded);
        return "redirect:/";
    }

    @GetMapping("/delete/item/{itemId}")
    public String deleteItem(@PathVariable Long itemId,
                             @CookieValue(value = "cart", defaultValue = "[]") String cartCookie,
                             HttpServletResponse response, RedirectAttributes redirectAttributes)
            throws JsonProcessingException {

        cartService.deleteItem(itemId, cartCookie, response);

        redirectAttributes.addFlashAttribute("isItemDeletedFromCart", true);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(HttpServletResponse response) {
        cartService.clearCart(response);

        return "redirect:/cart";
    }

    @PostMapping("/purchase")
    public String purchase(@CookieValue(value = "cart", defaultValue = "[]") String cartCookie,
                           HttpServletResponse response, RedirectAttributes redirectAttributes)
            throws JsonProcessingException {

        try {
            Receipt receipt = cartService.makePurchaseThenClearCart(cartCookie, response);
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "bad_request");
            clearCart(response);
        } catch (EmptyCartException ignored) {
        }

        return "redirect:/";

    }

}
