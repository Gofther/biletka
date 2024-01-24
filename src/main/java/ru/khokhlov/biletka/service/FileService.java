package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.dto.response.ImageHallSchemeResponse;

import java.io.IOException;
import ru.khokhlov.biletka.dto.response.EventImageResponse;
import ru.khokhlov.biletka.dto.universal.PublicEventImage;

import java.io.IOException;

@Service
public interface FileService {
    EventImageResponse postImageEvent(Long eventId,MultipartFile file) throws IOException;

    void postDocumentOrganization(MultipartFile file, Long id) throws IOException;

    ImageHallSchemeResponse postSchemeHall(MultipartFile file, Long id, Long organizationId) throws IOException;

    PublicEventImage getImageEvent(Long id);

    void getImagesEvents(String ids);

    void getSchemeHall(Long id);
}
