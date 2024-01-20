package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.entity.FileOrganization;
import ru.khokhlov.biletka.entity.Organization;
import ru.khokhlov.biletka.exception.ErrorMessage;
import ru.khokhlov.biletka.exception.InvalidDataException;
import ru.khokhlov.biletka.repository.FileOrganizationRepository;
import ru.khokhlov.biletka.service.EventService;
import ru.khokhlov.biletka.service.FileService;
import ru.khokhlov.biletka.service.OrganizationService;
import ru.khokhlov.biletka.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    private final EventService eventService;
    private final OrganizationService organizationService;
    private final FileOrganizationRepository fileOrganizationRepository;

    @Override
    public void postImageEvent(MultipartFile file) {

    }

    @Override
    public void postDocumentOrganization(MultipartFile file, Long id) throws IOException {
        if (organizationService.getOrganizationById(id).getFileOrganization() != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("File organization", "File organization is already exist!"));
            throw new InvalidDataException(errorMessages);
        }

        FileOrganization fileOrganization = new FileOrganization(
                file.getName(),
                file.getContentType(),
                FileUtils.compressFile(file.getBytes())
        );

        fileOrganizationRepository.saveAndFlush(fileOrganization);
        organizationService.addFileInOrganization(id, fileOrganization.getId());
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
