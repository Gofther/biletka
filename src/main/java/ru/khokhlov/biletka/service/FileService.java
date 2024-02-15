package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.dto.response.ImageHallSchemeResponse;

import java.io.IOException;
import ru.khokhlov.biletka.dto.response.EventImageResponse;
import ru.khokhlov.biletka.dto.universal.PublicEventImage;
import ru.khokhlov.biletka.entity.EventImage;
import ru.khokhlov.biletka.entity.FileOrganization;

import java.io.IOException;

@Service
public interface FileService {
    EventImageResponse postImageEvent(MultipartFile file) throws IOException;

    Long postDocumentOrganization(MultipartFile file) throws IOException;

    ImageHallSchemeResponse postSchemeHall(MultipartFile file, Long id, Long organizationId) throws IOException;

    PublicEventImage getImageEvent(Long id);

    FileOrganization getAllFileOrganization(Long id);

    EventImage getAllImageEvent(Long id);
}
