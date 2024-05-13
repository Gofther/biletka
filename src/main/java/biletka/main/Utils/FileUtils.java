package biletka.main.Utils;

import biletka.main.dto.universal.PublicEventImage;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
@Slf4j
public class FileUtils {
    @Value("${app.file.directory}")
    private String directory;

    /**
     * метод получения типа файла и проверка на многотиповость
     * @param fileName полновое название файла
     * @return тип файла
     */
    public String getFileExtension(String fileName) {
        Pattern pattern = Pattern.compile("\\.");
        String[] patternString = pattern.split(fileName);

        if (patternString.length != 2) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("File error", "The file is incorrect!"));
            throw new InvalidDataException(errorMessages);
        }

        return patternString[1];
    }

    /**
     * Метод проверки тип файла с разрешенными
     * @param typeFile тип файла
     * @param pattern разрешенные типы файла
     */
    public void validationFile(String typeFile, String[] pattern) {
        if (!Arrays.asList(pattern).contains(typeFile)) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("File error", "The file is incorrect!"));
            throw new InvalidDataException(errorMessages);
        }
    }

    /**
     * Метод сохранения файла в системе
     * @param file файла
     */
    public void fileUpload(MultipartFile file, String fileName) throws IOException {
        File convertFile = new File(directory+fileName);
        convertFile.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(convertFile);
        fileOut.write(file.getBytes());
        fileOut.close();
    }

    public PublicEventImage getFileEvent(String img) throws IOException {
        /** Получение файла */
        File file = new File(directory + "event/" + img);

        /** Получение содержание файла в массиве байтов */
        byte[] content = Files.readAllBytes(Paths.get(directory + "event/" + img));

        /** Получение mime типа файла */
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        String mimeType = URLConnection.guessContentTypeFromStream(inputStream);

        return new PublicEventImage(
                content,
                img,
                mimeType
        );
    }
}
