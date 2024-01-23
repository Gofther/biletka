package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.dto.response.EventImageResponse;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.service.EventService;
import ru.khokhlov.biletka.service.FileService;
import ru.khokhlov.biletka.entity.EventImage;
import ru.khokhlov.biletka.repository.EventImageRepository;
import ru.khokhlov.biletka.repository.EventRepository;
import ru.khokhlov.biletka.service.OrganizationService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    private final OrganizationService organizationService;
    private final EventImageRepository eventImageRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;
    @Override
    public EventImageResponse postImageEvent(Long eventId, MultipartFile file) throws IOException {
        try {
            if (file.isEmpty()) {
                log.trace("Файл пуст");
                throw new IOException("Файл пуст");
            }


            String fileName = file.getOriginalFilename();

            byte[] imageData = file.getBytes();

            EventImage eventImage = new EventImage(imageData, fileName, file.getContentType());
            System.out.println(eventImage);
            eventImageRepository.saveAndFlush(eventImage);

            log.trace("File {} uploaded successfully", fileName);

            eventService.addImageEvent(eventId, eventImage);
            return new EventImageResponse(
                    eventImage.getId(),
                    eventImage.getImageName(),
                    eventImage.getImageType()
            );

        } catch (IOException e) {
            log.trace("Failed to upload file", e);
            throw e;
        }
    }

    @Override
    public void postDocumentOrganization(MultipartFile file) {

    }

    @Override
    public void postSchemeHall(MultipartFile file) {

    }

    @Override
    public void getImageEvent(Long id) {

    }

    @Override
    public void getImagesEvents(String ids) {

    }

    @Override
    public void getSchemeHall(Long id) {

    }
}
