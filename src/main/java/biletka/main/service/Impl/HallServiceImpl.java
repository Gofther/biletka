package biletka.main.service.Impl;

import biletka.main.Utils.FileUtils;
import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.HallCreateRequest;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Hall;
import biletka.main.entity.Organization;
import biletka.main.entity.Place;
import biletka.main.entity.Users;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.HallRepository;
import biletka.main.service.*;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class HallServiceImpl implements HallService {
    private final HallRepository hallRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final MailSender mailSender;
    private final FileUtils fileUtils;

    private final UserService userService;
    private final OrganizationService organizationService;
    private final PlaceService placeService;

    /**
     * Метод создания зала площадки без схемы (требуется подтверждение администратор)
     * @param authorization токен авторизации
     * @param file схема зала
     * @param hallCreateRequestNew информация о зале
     * @return сообщение о успешном создании зала
     */
    @Override
    public MessageCreateResponse createHall(String authorization, MultipartFile file, HallCreateRequest hallCreateRequestNew) throws MessagingException, EntityNotFoundException {
        log.trace("HallServiceImpl.createHall - authorization {}, file {}, hallCreateRequestNew {}", authorization, file, hallCreateRequestNew);
        String typeFile = fileUtils.getFileExtension(file.getOriginalFilename());

        fileUtils.validationFile(
                typeFile,
                new String[]{"svg"}
        );

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

        Place place = placeService.getPlaceById(hallCreateRequestNew.placeId());

        if (!organization.getPlaceSet().contains(place)) {
            throw new EntityNotFoundException("A broken token!");
        }

        Hall hall = hallRepository.findFirstByPlaceAndHallNumber(place, hallCreateRequestNew.hallNumber());

        if (hall != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Create hall error", "This hall already exists!"));
            throw new InvalidDataException(errorMessages);
        }

        hall = new Hall(
                hallCreateRequestNew.hallNumber(),
                hallCreateRequestNew.hallName(),
                hallCreateRequestNew.numberOfSeats(),
                hallCreateRequestNew.info(),
                hallCreateRequestNew.seatGroupInfo(),
                null,
                place
        );

        hallRepository.saveAndFlush(hall);

        mailSender.sendHall(file, hall.getId());

        return new MessageCreateResponse(
                "The hall has been successfully created! Display wait for the administrator to check!"
        );
    }

    /**
     * Метод получения зала по id
     * @param id зала
     * @return зал
     */
    @Override
    public Hall getHallById(Long id) {
        log.trace("HallServiceImpl.getHallById - id {}", id);
        return hallRepository.getReferenceById(id);
    }

    /**
     * Метод получения количества залов в площадке
     * @param place площадка
     * @return количество залов
     */
    @Override
    public Integer getTotalByPlace(Place place) {
        log.trace("HallServiceImpl.getHallById - place {}", place);
        return hallRepository.findTotalByPlace(place);
    }

    /**
     * Метод получения массива залов по площадке
     * @param place площадка
     * @return массив залов
     */
    @Override
    public Set<Hall> getAllHallByPlace(Place place) {
        log.trace("HallServiceImpl.getAllHallByPlace - place {}", place);
        return hallRepository.findAllByPlace(place);
    }
}
