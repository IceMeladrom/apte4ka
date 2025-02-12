package ru.bmstu.my_apte4ka.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.bmstu.my_apte4ka.dto.ItemDto;
import ru.bmstu.my_apte4ka.entity.Item;
import ru.bmstu.my_apte4ka.repository.ItemRepository;
import ru.bmstu.my_apte4ka.utils.ImageProcessor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;


    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public ResponseEntity<Item> create(ItemDto itemDto, MultipartFile image) throws IOException {
        String pathToImage = "";
        if (!image.isEmpty()) {
            String targetPath = Paths.get("uploads/" + image.getOriginalFilename()).toAbsolutePath().toString();

            ImageProcessor.saveMultipartFileAsPng(image, targetPath);

            pathToImage = "/uploads/" + image.getOriginalFilename();
        }

        Item item = Item.builder()
                .name(itemDto.getName())
                .price(itemDto.getPrice())
                .quantity(itemDto.getQuantity())
                .pathToImage(pathToImage)
                .build();

        Item savedItem = itemRepository.save(item);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    public Item get(Long id) throws EntityNotFoundException {
        return itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Item update(Long id, ItemDto newItem, MultipartFile image) throws IOException {
        Item oldItem = itemRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        oldItem.setName(newItem.getName());
        oldItem.setPrice(newItem.getPrice());
        oldItem.setQuantity(newItem.getQuantity());

        if (!image.isEmpty()) {
            String targetPath = Paths.get("uploads/" + image.getOriginalFilename()).toAbsolutePath().toString();

            ImageProcessor.saveMultipartFileAsPng(image, targetPath);

            oldItem.setPathToImage("/uploads/" + image.getOriginalFilename());
        }

        return itemRepository.save(oldItem);
    }
}
