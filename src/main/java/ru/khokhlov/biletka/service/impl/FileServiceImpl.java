package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.service.FileService;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    @Override
    public void postImageEvent(MultipartFile file) {

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
