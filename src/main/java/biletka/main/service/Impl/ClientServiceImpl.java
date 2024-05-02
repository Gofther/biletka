package biletka.main.service.Impl;

import biletka.main.dto.request.ClientRegistrationRequest;
import biletka.main.entity.Client;
import biletka.main.entity.Users;
import biletka.main.enums.StatusUserEnum;
import biletka.main.repository.ClientRepository;
import biletka.main.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

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
}
