package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
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
import ru.khokhlov.biletka.dto.response.EventImageResponse;
import ru.khokhlov.biletka.dto.universal.PublicEventImage;
import ru.khokhlov.biletka.entity.Event;
import ru.khokhlov.biletka.service.FileService;
import ru.khokhlov.biletka.entity.EventImage;
import ru.khokhlov.biletka.repository.EventImageRepository;
import ru.khokhlov.biletka.repository.EventRepository;
import ru.khokhlov.biletka.service.OrganizationService;



@Service
@RequiredArgsConstructor(onConstructor = @__(@org.springframework.context.annotation.Lazy))
@Slf4j
public class FileServiceImpl implements FileService {
    @Lazy
    private final OrganizationService organizationService;
    private final EventImageRepository eventImageRepository;
    private final EventRepository eventRepository;
    private final FileOrganizationRepository fileOrganizationRepository;
    private final PlaceService placeService;
    private final MailSender mailSender;

    @Override
    public EventImageResponse postImageEvent(MultipartFile file) throws IOException {
        try {
            if (file.isEmpty()) {
                log.trace("Файл пуст");
                throw new IOException("Файл пуст");
            }


            String fileName = file.getOriginalFilename();

            byte[] imageData = file.getBytes();

            EventImage eventImage = new EventImage(imageData, fileName, file.getContentType());
            eventImageRepository.saveAndFlush(eventImage);

            log.trace("File {} uploaded successfully", fileName);

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
    public Long postDocumentOrganization(MultipartFile file) throws IOException {
        FileOrganization fileOrganization = new FileOrganization(
                file.getName(),
                file.getContentType(),
                FileUtils.compressFile(file.getBytes())
        );

        fileOrganizationRepository.saveAndFlush(fileOrganization);

        return fileOrganization.getId();
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
    public PublicEventImage getImageEvent(Long id) {
        Event event = eventRepository.getReferenceById(id);
        System.out.println();
        return new PublicEventImage(
                event.getEventBasicInformation().getEventImage().getImageData(),
                event.getEventBasicInformation().getEventImage().getImageName(),
                event.getEventBasicInformation().getEventImage().getImageType()
        );
    }

    @Override
    public FileOrganization getAllFileOrganization(Long id) {
        return fileOrganizationRepository.getReferenceById(id);
    }

    @Override
    public EventImage getAllImageEvent(Long id) {
        return eventImageRepository.getReferenceById(id);
    }
}
