package ru.khokhlov.biletka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.dto.response.EventImageResponse;
import ru.khokhlov.biletka.service.FileService;
import ru.khokhlov.biletka.entity.EventImage;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
@Tag(name = "Контроллер файлов", description = "")
@Slf4j


public class FileController {
    private final FileService fileService;
//   TODO Response пишеться в dto/response

//    @Getter
//    @AllArgsConstructor
//    public class FileInfoResponse {
//        private String fileName;
//        private String contentType;
//        private long size;
//    }
//
//    @Operation(
//            summary = "Загрузка файла",
//            description = "Принимает файл и выводит информацию о нём"
//    )
//    @PostMapping("/upload")
//    public ResponseEntity<FileInfoResponse> handleFileUpload(@RequestParam("file") MultipartFile file ) {
    // TODO вся проверка и работа происходит в Service
//     if (file.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        }
//
//            String fileName = file.getOriginalFilename();
//            String contentType = file.getContentType();
//            long size = file.getSize();
//
//            log.trace("Received file:");
//            log.trace("Name: {}", fileName);
//            log.trace("Content Type: {}", contentType);
//            log.trace("Size: {} bytes", size);
//        FileInfoResponse fileInfoResponse = new FileInfoResponse(fileName, contentType, size);
//
//        return ResponseEntity.status(HttpStatus.OK).body(fileInfoResponse);
//    }

    @Operation(
            summary = "Загрузка изображения для мероприятия",
            description = "Принимает файл jpg,png и тд и сохраняет в бд"
    )
    @PostMapping("/event")
    public ResponseEntity<EventImageResponse> postImageEvent(@RequestParam("eventId") Long eventId, @RequestParam("file") MultipartFile file) {
        try {
            EventImageResponse response = fileService.postImageEvent(eventId,file);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @Operation(
            summary = "Загрузка файла организации",
            description = "Принимает файл подписания услуг с орагнизацией и сохраняет в бд"
    )
    @PostMapping("/organization")
    public void postFileOrganization(@RequestParam("file") MultipartFile file) {
        log.trace("FileController.postFileOrganization /file/organization - file {}", file);
    }

    @Operation(
            summary = "Загрузка изображения зала",
            description = "Принимает файл jpg, png и тд и сохраняет в бд"
    )
    @PostMapping("/hall")
    public void postImageHall(@RequestParam("file") MultipartFile file) {
        log.trace("FileController.postImageHall /file/hall - file {}", file);
    }
}