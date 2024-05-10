package biletka.main.service.Impl;

import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.PlaceCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.City;
import biletka.main.entity.Organization;
import biletka.main.entity.Place;
import biletka.main.entity.Users;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.CityRepository;
import biletka.main.repository.PlaceRepository;
import biletka.main.repository.UserRepository;
import biletka.main.service.OrganizationService;
import biletka.main.service.PlaceService;
import biletka.main.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final CityRepository cityRepository;
    private final UserService userService;
    private final OrganizationService organizationService;

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public MessageCreateResponse postPlaceCreate(String authorization, PlaceCreateRequest placeCreateRequest) throws EntityNotFoundException {
        log.trace("PlaceServiceImpl.postPlaceCreate - authorization {}, placeCreateRequest {}", authorization, placeCreateRequest);
        String userEmail = jwtTokenUtils.getUsernameFromToken(
                authorization.substring(7)
        );

        Users user = userService.getUserOrganizationByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Organization organization = organizationService.getOrganizationByUser(user);

        if (organization == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Place place = placeRepository.findFirstByAddressAndPlaceName(placeCreateRequest.address(), placeCreateRequest.placeName());
        City city = cityRepository.findFirstByCityNameEng(placeCreateRequest.city());

        if (place != null || city == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Place error", "This place already exists or the city does not exist!"));
            throw new InvalidDataException(errorMessages);
        }

        Place newPlace = new Place(
                placeCreateRequest.address(),
                placeCreateRequest.placeName(),
                new Timestamp(new Date().getTime()),
                city
        );

        placeRepository.saveAndFlush(newPlace);

        organizationService.addPlace(organization, newPlace);

        return new MessageCreateResponse(
                "The place '" + newPlace.getPlaceName() + "' at the address '" + newPlace.getAddress() + "' has been successfully created!"
        );
    }

    /**
     * Метод полечния площадки
     * @param id id площадки
     * @return площадка
     */
    @Override
    public Place getPlaceById(Long id) throws EntityNotFoundException {
        log.trace("PlaceServiceImpl.getPlaceById - id {}", id);
        return placeRepository.getReferenceById(id);
    }
}
