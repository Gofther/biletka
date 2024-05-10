package biletka.main.service.Impl;

import biletka.main.Utils.JwtTokenUtils;
import biletka.main.dto.request.ClientRegistrationRequest;
import biletka.main.dto.response.FavoriteResponse;
import biletka.main.entity.Client;
import biletka.main.entity.Event;
import biletka.main.entity.Users;
import biletka.main.enums.StatusUserEnum;
import biletka.main.repository.ClientRepository;
import biletka.main.service.ClientService;
import biletka.main.service.EventService;
import biletka.main.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Lazy
    private final UserService userService;
    private final EventService eventService;

    /**
     * Метод добавления нового пользователя в бд
     * @param client данные пользователя
     * @param user данные для входа
     */
    @Override
    public void postNewClient(ClientRegistrationRequest client, Users user) throws ParseException {
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

        String userEmail = jwtTokenUtils.getUsernameFromToken(
                authorization.substring(7)
        );

        Users user = userService.getUserByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Client client = clientRepository.findFirstByUser(user);
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
}
