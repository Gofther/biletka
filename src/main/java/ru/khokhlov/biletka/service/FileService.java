package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.dto.response.ImageHallSchemeResponse;

import java.io.IOException;

@Service
public interface FileService {
    void postImageEvent(MultipartFile file);

    void postDocumentOrganization(MultipartFile file, Long id) throws IOException;

    ImageHallSchemeResponse postSchemeHall(MultipartFile file, Long id, Long organizationId) throws IOException;

    void getImageEvent(Long id);

    void getImagesEvents(String ids);

    void getSchemeHall(Long id);
}
