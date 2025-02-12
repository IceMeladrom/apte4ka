package ru.bmstu.my_apte4ka.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.bmstu.my_apte4ka.dto.ItemDto;
import ru.bmstu.my_apte4ka.service.ItemService;
import ru.bmstu.my_apte4ka.service.StatisticsService;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ItemService itemService;
    private final StatisticsService statisticsService;

    @GetMapping()
    public String getAdminPage(Model model) {
        model.addAttribute("topSellingItem", statisticsService.getTopSellingItem());
        model.addAttribute("worstSellingItem", statisticsService.getWorstSellingItem());
        model.addAttribute("theMostExpensiveItem", statisticsService.getTheMostExpensiveItem());
        model.addAttribute("theMostCheapestItem", statisticsService.getTheMostCheapestItem());
        model.addAttribute("theLargestQuantityOfItems", statisticsService.getTheLargestQuantityOfItems());
        model.addAttribute("theSmallestQuantityOfItems", statisticsService.getTheSmallestQuantityOfItems());

        return "admin";
    }

    @GetMapping("/items/create")
    public String getCreateItemPage(Model model) {
        return "create-item";
    }


    @PostMapping("/items/create")
    public String createItem(@ModelAttribute ItemDto itemDto,
                             @RequestParam(name = "pathToImage", required = false) MultipartFile image,
                             RedirectAttributes redirectAttributes) throws IOException {
        itemService.create(itemDto, image);
        redirectAttributes.addFlashAttribute("isItemCreated", true);
        return "redirect:/admin";
    }

    @GetMapping("/items/update")
    public String getUpdateItemPage(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "update-item";
    }

    @GetMapping("/items/update/{itemId}")
    public String getUpdateSpecificItemPage(@PathVariable Long itemId, Model model) {
        model.addAttribute("item", itemService.get(itemId));
        return "update-specific-item";
    }

    @PostMapping("/items/update/{itemId}")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute ItemDto newItem,
                             @RequestParam(name = "pathToImage", required = false) MultipartFile image,
                             Model model, RedirectAttributes redirectAttributes) throws IOException {
        itemService.update(itemId, newItem, image);
        redirectAttributes.addFlashAttribute("isItemEdited", true);
        return "redirect:/admin";
    }


}
