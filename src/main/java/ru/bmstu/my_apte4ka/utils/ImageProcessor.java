package ru.bmstu.my_apte4ka.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageProcessor {

    public static void saveMultipartFileAsPng(MultipartFile multipartFile, String outputFilePath) throws IOException {
        // Если точно такой же файл уже существует, то просто завершаем работу
        if (Files.exists(Paths.get(outputFilePath)))
            return;

        // Преобразование MultipartFile в BufferedImage
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());

        // Проверка, удалось ли прочитать изображение
        if (image == null) {
            throw new IOException("Не удалось прочитать изображение из загруженного файла.");
        }

        // Создание выходного файла
        File outputFile = new File(outputFilePath);

        // Сохранение BufferedImage в формате PNG
        boolean result = ImageIO.write(image, "png", outputFile);

        if (!result) {
            throw new IOException("Не удалось сохранить изображение в формате PNG.");
        }
    }
}
