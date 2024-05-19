package biletka.main.service.Impl;

import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.ClientRegistrationRequest;
import biletka.main.dto.request.RatingClientRequest;
import biletka.main.dto.response.FavoriteResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.dto.universal.MassivePublicEvent;
import biletka.main.dto.universal.PublicEvent;
import biletka.main.entity.*;
import biletka.main.enums.StatusUserEnum;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.ClientRepository;
import biletka.main.service.ClientService;
import biletka.main.service.EventService;
import biletka.main.service.RatingService;
import biletka.main.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Lazy
    private final UserService userService;
    private final EventService eventService;
    private final RatingService ratingService;

    /**
     * Метод добавления нового пользователя в бд
     * @param client данные пользователя
     * @param user данные для входа
     */
    @Override
    public void postNewClient(ClientRegistrationRequest client, Users user) throws ParseException {
        log.trace("ClientServiceImpl.postNewClient - client {}, user {}", client, user);
        Client newClient = new Client(
            user,
            client.fullName(),
            new Timestamp(client.birthday().getTime()),
            client.phoneNumber(),
            new Timestamp(new Date().getTime()),
            StatusUserEnum.INACTIVE
        );

        clientRepository.saveAndFlush(newClient);
    }

    /**
     * Метод изменения мероприятия в избранном пользователя
     * @param authorization токен авторизации
     * @param id мероприятия
     * @return id и измененный статус избранного
     */
    @Override
    public FavoriteResponse toggleEventFavorite(String authorization, Long id) throws EntityNotFoundException {
        log.trace("ClientServiceImpl.toggleEventFavorite - authorization {}, id {}", authorization, id);
        String userEmail = jwtTokenUtils.getUsernameFromToken(
                authorization.substring(7)
        );

        Client client = tokenVerification(authorization);
        Event event = eventService.getEventById(id);

        if (event == null) {
            throw new EntityNotFoundException("There is no event with this id!");
        }

        boolean eventFavorite = client.getEventSet().contains(event);

        if (eventFavorite) {
            client.getEventSet().remove(event);
        } else {
            client.addEvent(event);
        }
        clientRepository.save(client);

        return new FavoriteResponse(
                event.getId(),
                !eventFavorite
        );
    }

    /**
     * Метод получения массива мероприятий из таблицы избранное у пользователя
     * @param authorization токен авторизации
     * @return массив мероприятий
     */
    @Override
    public MassivePublicEvent getFavorite(String authorization) {
        log.trace("ClientServiceImpl.getFavorite - authorization {}", authorization);

        Client client = tokenVerification(authorization);

        ArrayList<PublicEvent> publicEvents = new ArrayList<>();

        client.getEventSet().forEach(event -> {
            Set<String> genres = new HashSet<>();

            event.getEventBasicInformation().getGenres().forEach(genre -> genres.add(genre.getName()));

            publicEvents.add(new PublicEvent(
                    event.getId(),
                    event.getEventBasicInformation().getName_rus(),
                    event.getEventBasicInformation().getSymbolicName(),
                    event.getEventBasicInformation().getAgeRatingId().getLimitation(),
                    genres.toArray(String[]::new),
                    event.getEventBasicInformation().getImg(),
                    event.getEventBasicInformation().getTypeEventId().getType(),
                    true
            ));
        });

        return new MassivePublicEvent(publicEvents.toArray(PublicEvent[]::new));
    }

    /**
     * Метод получения клиента
     * @param user почта пользователя
     * @return клиент
     */
    @Override
    public Client getClientByUser(Users user) {
        log.trace("ClientServiceImpl.getClientByUser - user {}", user);
        return clientRepository.findFirstByUser(user);
    }

    /**
     * Метод изменения рейтинга мероприятия пользователем
     * @param authorization токен авторизации
     * @param ratingClientRequest информация для изменения рейтинга мероприятия
     * @return успешное изменение
     */
    @Override
    public MessageCreateResponse putRatingEvent(String authorization, RatingClientRequest ratingClientRequest) {
        log.trace("ClientServiceImpl.putRatingEvent - authorization {}, ratingClientRequest {}", authorization, ratingClientRequest);

        Client client = tokenVerification(authorization);

        Event event = eventService.getEventByIdAndSymbolic(ratingClientRequest.eventSymbolic());

        ratingService.createRating(client, event, ratingClientRequest.rating());

        eventService.putRatingEvent(event, ratingClientRequest.rating());

        return new MessageCreateResponse(
                "The rating has been successfully set!"
        );
    }
    /**
     * Проверка токена авторизации и вывод пользователя
     * @param token токен авторизации
     * @return организация
     */
    public Client tokenVerification(String token) {
        log.trace("ClientServiceImpl.tokenVerification - token {}", token);
        String userEmail = jwtTokenUtils.getUsernameFromToken(
                token.substring(7)
        );

        Users user = userService.getUserByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Client client = clientRepository.findFirstByUser(user);

        if (client == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        return client;
    }

}
