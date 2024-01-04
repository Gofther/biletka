package ru.khokhlov.biletka.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.PlaceInfo;
import ru.khokhlov.biletka.dto.response.PlaceResponse;
import ru.khokhlov.biletka.entity.City;
import ru.khokhlov.biletka.entity.Organization;
import ru.khokhlov.biletka.entity.Place;
import ru.khokhlov.biletka.repository.CityRepository;
import ru.khokhlov.biletka.repository.PlaceRepository;
import ru.khokhlov.biletka.service.OrganizationService;
import ru.khokhlov.biletka.service.PlaceService;
import ru.khokhlov.biletka.utils.NameRebuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final CityRepository cityRepository;
    @Lazy
    private final OrganizationService organizationService;


    @Override
    public PlaceResponse createPlace(String city, PlaceInfo placeInfo) throws EntityExistsException {
        log.trace("PlaceServiceImpl.createPlace - city {}, placeInfo {}", city, placeInfo);

        if (placeRepository.getFirstByNameAndCity(NameRebuilder.rebuildName(placeInfo.name()), cityRepository.findFirstByNameEng(city)) != null)
            throw new EntityExistsException(
                    "Place with city "
                            + city +
                            " and name "
                            + NameRebuilder.rebuildName(placeInfo.name())
                            + " already exists!");

        Place place = new Place(
                NameRebuilder.rebuildName(placeInfo.name()),
                placeInfo.address()
        );

        place.setCity(
                cityRepository.findFirstByNameEng(city)
        );
        placeRepository.saveAndFlush(place);
        return new PlaceResponse(
                place.getName(),
                place.getCity().getNameEng(),
                place.getAddress(),
                new ArrayList<>()
        );
    }

    @Override
    public Place getPlaceById(Long placeId) throws EntityNotFoundException{
        return placeRepository.getReferenceById(placeId);
    }



    @Override
    public Place getPlaceByCity(String city) {
        log.trace("PlaceServiceImpl.getPlaceByCity - city {}", city);

        Place place =  placeRepository.getFirstByCity(
                cityRepository.findFirstByNameEng(city)
        );

        if (place == null)
            throw new EntityNotFoundException(
                    "Place with city "
                            + city + " does not exists!");

        return place;
    }


    @Override
    public Place getPlaceByNameAndCity(String name, String city) {
        log.trace("PlaceServiceImpl.getPlaceByNameAndCity - name {}, city {}", name, city);

        Place place = placeRepository.getFirstByNameAndCity(
                NameRebuilder.rebuildName(name),
                cityRepository.findFirstByNameEng(city)
        );

        if (place == null)
            throw new EntityNotFoundException(
                    "Place with city "
                            + city +
                            " and name "
                            + NameRebuilder.rebuildName(name)
                            + " does not exists!");

        return place;
    }

    @Override
    public List<Place> getAllPlaceByOrganization(String city, Long organizationId) throws EntityNotFoundException {
        log.trace("HallSchemeServiceImpl.getAllPlaceByOrganization - City {}, OrganizationId {}", city, organizationId);

        Organization organization = organizationService.getOrganizationById(organizationId);
        List<Place> placeList = new ArrayList<>(organization.getPlaceSet());

        log.trace("HallSchemeServiceImpl.getAllPlaceByOrganization - List<placeList> {}", placeList);
        return placeList;
    }

    @Override
    public Set<Place> getAllByCity(City city) {
        Set<Place> placeSet = placeRepository.getAllByCity(city);

        if(placeSet.isEmpty()){
            throw new EntityNotFoundException("Places with city " + city + " not found");
        }
        return placeSet;
    }
}
