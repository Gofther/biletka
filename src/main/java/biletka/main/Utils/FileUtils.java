package biletka.main.Utils;

import biletka.main.dto.universal.PublicEventImage;
import biletka.main.dto.universal.PublicHallFile;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
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
    @Value("${app.file.directory.event}")
    private String directoryEvent;

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
        File convertFile = new File(directoryEvent+fileName);
        convertFile.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(convertFile);
        fileOut.write(file.getBytes());
        fileOut.close();
    }

    public PublicEventImage getFileEvent(String img) throws IOException {
        /** Получение файла */
        File file = new File(directoryEvent + img);

        /** Получение содержание файла в массиве байтов */
        byte[] content = Files.readAllBytes(Paths.get(directoryEvent + img));

        /** Получение mime типа файла */
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        String mimeType = URLConnection.guessContentTypeFromStream(inputStream);

        return new PublicEventImage(
                content,
                img,
                mimeType
        );
    }
    @Value("${app.file.directory.hall}")
    private String directoryHall;

    /**
     * Метод сохранения svg файла для зала
     * @param file файл
     */
    public void fileUploadHall(MultipartFile file, String fileName) throws IOException {
        File convertFile = new File(directoryHall+fileName);
        convertFile.createNewFile();
        FileOutputStream fileOut = new FileOutputStream(convertFile);
        fileOut.write(file.getBytes());
        fileOut.close();
    }

    public PublicHallFile getFileHall(String filename) throws IOException{
        StringBuilder textContent = new StringBuilder();
        File svgFile = null;
            svgFile = new File(directoryHall + filename);
        try (BufferedReader br = new BufferedReader(new FileReader(svgFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                textContent.append(line).append("\n");
            }
        }
        return new PublicHallFile(
                textContent.toString().trim(),
                svgFile.getName()
        );
    }

}
