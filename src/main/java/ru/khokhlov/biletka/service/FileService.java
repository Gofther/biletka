package ru.khokhlov.biletka.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {
    void postImageEvent(MultipartFile file);

    void postDocumentOrganization(MultipartFile file);

    void postSchemeHall(MultipartFile file);

    void getImageEvent(Long id);

    void getImagesEvents(String ids);

    void getSchemeHall(Long id);
}
