package ru.bmstu.my_apte4ka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.bmstu.my_apte4ka.service.ItemService;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final ItemService itemService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "index";
    }
}
