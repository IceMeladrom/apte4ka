package ru.bmstu.my_apte4ka.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.bmstu.my_apte4ka.service.CartService;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CartService cartService;

    @Override
    public void run(String... args) throws Exception {
    }
}
