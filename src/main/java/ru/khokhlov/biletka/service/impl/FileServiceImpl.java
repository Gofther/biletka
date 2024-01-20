package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.khokhlov.biletka.dto.response.ImageHallSchemeResponse;
import ru.khokhlov.biletka.entity.FileOrganization;
import ru.khokhlov.biletka.entity.Organization;
import ru.khokhlov.biletka.entity.Place;
import ru.khokhlov.biletka.exception.ErrorMessage;
import ru.khokhlov.biletka.exception.InvalidDataException;
import ru.khokhlov.biletka.repository.FileOrganizationRepository;
import ru.khokhlov.biletka.service.*;
import ru.khokhlov.biletka.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {
    private final EventService eventService;
    private final OrganizationService organizationService;
    private final FileOrganizationRepository fileOrganizationRepository;
    private final PlaceService placeService;
    private final MailSender mailSender;

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
    public ImageHallSchemeResponse postSchemeHall(MultipartFile file, Long id, Long organizationId) throws IOException {
        if (!Arrays.asList(new String[] {"image/jpeg", "image/png", "application/xml"}).contains(file.getContentType())) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Scheme file", "Schema file is of the wrong type!"));
            throw new InvalidDataException(errorMessages);
        }

        Organization organization = organizationService.getOrganizationById(organizationId);
        Place place = placeService.getPlaceById(id);

        if (!organization.getPlaceSet().contains(place)) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Organization or Place", "Organization or place does not exist!"));
            throw new InvalidDataException(errorMessages);
        }

        mailSender.confirmationHallScheme(file, organization.getId(), organization.getEmail(), place.getId(), place.getName());

        return new ImageHallSchemeResponse("Ожидайте подтверждение схемы зала");
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
