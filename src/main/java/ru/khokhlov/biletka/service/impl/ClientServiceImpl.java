package ru.khokhlov.biletka.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.ClientRegistration;
import ru.khokhlov.biletka.dto.request.UserId;
import ru.khokhlov.biletka.dto.response.ClientResponse;
import ru.khokhlov.biletka.dto.universal.PublicClient;
import ru.khokhlov.biletka.entity.Client;
import ru.khokhlov.biletka.entity.Organization;
import ru.khokhlov.biletka.enums.RoleEnum;
import ru.khokhlov.biletka.repository.ClientRepository;
import ru.khokhlov.biletka.service.ClientService;
import ru.khokhlov.biletka.service.MailSender;
import ru.khokhlov.biletka.service.OrganizationService;
import ru.khokhlov.biletka.utils.PasswordEncoder;

import java.util.Collections;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService, UserDetailsService {
    private final OrganizationService organizationService;
    private final ClientRepository clientRepository;
    private final MailSender mailSender;


    @Override
    public Long getClientIdByEmailAndPassword(String email, String password) {
        Long id = -1L;
        Client client = clientRepository.findByEmail(email);

        if (client != null && PasswordEncoder.arePasswordsEquals(password, client.getPassword()))
            id = client.getId();

        return id;
    }

    @Override
    public ClientResponse createClient(ClientRegistration clientRegistration) throws EntityExistsException {
        log.trace("ClientServiceImpl.createClient - clientRegistration {}", clientRegistration);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (clientRepository.findByEmail(clientRegistration.email()) != null) {
            throw new EntityExistsException("User with email: " + clientRegistration.email() + " already exist!");
        }

        //TODO Mapper
        Client client = new Client(
                clientRegistration.fullName(),
                clientRegistration.phoneNumber(),
                clientRegistration.email(),
                generateActivationCode(),
                passwordEncoder.encode(clientRegistration.password()),
                clientRegistration.birthday(),
                RoleEnum.USER
        );
        clientRepository.saveAndFlush(client);
        client.setPassword(clientRegistration.password());

        mailSender.activateEmailClient(client);
        return new ClientResponse(client);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, EntityNotFoundException {
        log.trace("ClientServiceImpl.loadUserByUsername - username {}", email);

        Client client = clientRepository.findByEmail(email);
        Organization organization = organizationService.getOrganizationByEmail(email);

        if (client == null && organization == null) {
            throw new EntityNotFoundException("Entity with email " + email + " not found");
        }

        if (client != null) {
            return new User(
                    client.getEmail(),
                    client.getPassword(),
                    Collections.singleton(client.getRoleEnum())
            );
        }

        return new User(
                organization.getEmail(),
                organization.getPassword(),
                Collections.singleton(organization.getRoleEnum())
        );
    }

    @Override
    public boolean isEmailActivate(String code) throws EntityNotFoundException {
        Client client = clientRepository.findByActivationCode(code);

        if (client == null) {
            throw new EntityNotFoundException("This code is not exists!");
        }

        client.setActivationCode(null);
        clientRepository.saveAndFlush(client);

        return true;
    }

    @Override
    public PublicClient infoClient(Integer userId) throws EntityNotFoundException{
        Client client = clientRepository.getReferenceById(Long.valueOf(userId));
        System.out.println(client);
        return new PublicClient(
                client.getId(),
                client.getFullName(),
                "+"+client.getPhone(),
                client.getEmail(),
                String.valueOf(client.getBirthday()).substring(0,10)
        );
    }

    private String generateActivationCode() {
        log.debug("UserServiceImpl.generateActivationCode");

        String activationCode = "";
        boolean isCodeUniq = false;

        while (!isCodeUniq) {
            activationCode = UUID.randomUUID().toString();

            if (clientRepository.findByActivationCode(activationCode) == null)
                isCodeUniq = true;
        }

        log.debug("UserServiceImpl.generateActivationCode - activationCode {}", activationCode);
        return activationCode;
    }
}
