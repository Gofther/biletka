package ru.khokhlov.biletka.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.HallCreationRequestDTO;
import ru.khokhlov.biletka.dto.response.HallCreationResponseDTO;
import ru.khokhlov.biletka.entity.HallScheme;
import ru.khokhlov.biletka.entity.Organization;
import ru.khokhlov.biletka.entity.Place;
import ru.khokhlov.biletka.repository.HallSchemeRepository;
import ru.khokhlov.biletka.service.HallSchemeService;
import ru.khokhlov.biletka.service.OrganizationService;
import ru.khokhlov.biletka.service.PlaceService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HallSchemeServiceImpl implements HallSchemeService {
    private final OrganizationService organizationService;
    private final PlaceService placeService;
    private final HallSchemeRepository hallSchemeRepository;

    @Override
    public HallScheme getHallScheme(Long id) throws EntityNotFoundException {
        log.trace("HallSchemeServiceImpl.getHallScheme - id {}", id);
        return hallSchemeRepository.getReferenceById(id);
    }

    @Override
    public HallCreationResponseDTO createHallScheme(String city, HallCreationRequestDTO hallCreationRequestDTO) throws EntityNotFoundException {
        log.trace("HallSchemeServiceImpl.createHallScheme - city {}, HallCreationRequestDTO {}", city, hallCreationRequestDTO);

        Place place = placeService.getPlaceById(hallCreationRequestDTO.placeId());

        HallScheme hallScheme = new HallScheme(
                hallCreationRequestDTO.name(),
                hallCreationRequestDTO.info(),
                hallCreationRequestDTO.floor(),
                hallCreationRequestDTO.hallNumber(),
                hallCreationRequestDTO.seatsCount(),
                hallCreationRequestDTO.file(),
                place);

        try {
            hallSchemeRepository.saveAndFlush(hallScheme);
            Long hallId = hallScheme.getId();

            log.trace("HallSchemeServiceImpl.createHallScheme - HallId {}", hallId);
            return new HallCreationResponseDTO(hallId);
        }
        catch (RuntimeException ex){
            throw new EntityNotFoundException("Place with id " + hallCreationRequestDTO.placeId() + " not found");
        }
    }

    @Override
    public List<HallScheme> getAllHallByOrganization(String city, Long organizationId) throws EntityNotFoundException {
        log.trace("HallSchemeServiceImpl.getAllPlaceByOrganization - City {}, OrganizationId {}", city, organizationId);

        Organization organization = organizationService.getOrganizationById(organizationId);
        List<Place> placeList = new ArrayList<>(organization.getPlaceSet());
        List<HallScheme> hallSchemeList = new ArrayList<>();

        for (Place place : placeList) {
            hallSchemeList.addAll(hallSchemeRepository.findAllByPlace(place));
        }

        log.trace("HallSchemeServiceImpl.getAllPlaceByOrganization - List<HallScheme> {}", hallSchemeList);
        return hallSchemeList;
    }


}
