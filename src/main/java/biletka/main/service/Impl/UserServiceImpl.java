package biletka.main.service.Impl;

import biletka.main.Utils.ActivationCode;
import biletka.main.Utils.JwtTokenUtils;
import biletka.main.Utils.PasswordEncoder;
import biletka.main.dto.request.ActiveClientRequest;
import biletka.main.dto.request.AuthForm;
import biletka.main.dto.request.ClientRegistrationRequest;
import biletka.main.dto.request.OrganizationRegistrationRequest;
import biletka.main.dto.response.AuthResponse;
import biletka.main.dto.response.ClientRegistrationResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Administrator;
import biletka.main.entity.Organization;
import biletka.main.entity.Users;
import biletka.main.enums.RoleEnum;
import biletka.main.enums.StatusUserEnum;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.UserRepository;
import biletka.main.service.*;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final ActivationCode activationCode;

    @Lazy
    private final ClientService clientService;
    private final OrganizationService organizationService;
    private final AdministratorService administratorService;
    private final MailSender mailSender;

    /**
     * Метод получения токена авторизации
     * @param authForm форма аутентификации
     * @return токен авторизации
     */
    @Override
    @Transactional
    public AuthResponse getAuthToken(AuthForm authForm) {
        log.trace("UserServiceImpl.getAuthToken - authForm {}", authForm);
        Users user = userRepository.findFirstByEmail(authForm.email());

        // Проверка на правильно введнные данные и не актвиный аккаунт
        if (user == null ||
                user.getStatus().getStatusUser().equalsIgnoreCase("INACTIVE") ||
                user.getStatus().getStatusUser().equalsIgnoreCase("ACTIVE") &&
                (
                        !user.getRole().getAuthority().equalsIgnoreCase(authForm.role()) ||
                        PasswordEncoder.arePasswordsEquals(user.getPassword(), authForm.password())
                )
        ) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Authentication error", "The email or password is incorrect!"));
            throw new InvalidDataException(errorMessages);
        }
        // Проверка на бан аккаунта
        else if (user.getStatus().getStatusUser().equalsIgnoreCase("BANNED")) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Banned", "The account is banned!"));
            throw new InvalidDataException(errorMessages);
        }

        // Детали пользователя для токена
        UserDetails userDetails = new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );

        return new AuthResponse(
                jwtTokenUtils.generateToken(userDetails)
        );
    }

    /**
     * Метод сохранения нового пользователя в бд
     * @param client данные пользователя
     * @return сообщение о успешном создании пользователя
     */
    @Override
    public ClientRegistrationResponse postNewUser(ClientRegistrationRequest client) throws ParseException, MessagingException {
        log.trace("UserServiceImpl.postNewUser - client {}", client);

        Users user = userRepository.findFirstByEmail(client.email());

        if (user != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Registration error", "This user already exists!"));
            throw new InvalidDataException(errorMessages);
        }

        Users newUser = new Users(
                client.email(),
                PasswordEncoder.getEncryptedPassword(client.password()),
                RoleEnum.CLIENT,
                StatusUserEnum.INACTIVE,
                activationCode.generateActivationCode()
        );

        userRepository.saveAndFlush(newUser);

        clientService.postNewClient(client, newUser);

        mailSender.activateEmailClient(newUser.getActiveCode(), newUser.getEmail());

        return new ClientRegistrationResponse(
                String.format("The user '" + newUser.getEmail() + "' has been created")
        );
    }

    /**
     * Метод сохранения нового организации в бд
     * @param organizationRequest данные организации
     * @return сообщение о успешном создании организации
     */
    @Override
    public MessageCreateResponse postNewOrganization(OrganizationRegistrationRequest organizationRequest) {
        log.trace("UserServiceImpl.postNewOrganization - organizationRequest {}", organizationRequest);

        Users user = userRepository.findFirstByEmail(organizationRequest.email());
        Organization organization = organizationService.getOrganizationByFullNameOrganization(organizationRequest);

        if (user != null || organization != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Registration error", "This user already exists!"));
            throw new InvalidDataException(errorMessages);
        }

        Users newUser = new Users(
                organizationRequest.email(),
                PasswordEncoder.getEncryptedPassword(organizationRequest.password()),
                RoleEnum.ORGANIZATION,
                StatusUserEnum.ACTIVE,
                activationCode.generateActivationCode()
        );

        userRepository.saveAndFlush(newUser);

        organizationService.postCreateOrganization(organizationRequest, newUser);

        return new MessageCreateResponse(
                "The organization '" + organizationRequest.fullNameOrganization() + "' has been created"
        );
    }

    /**
     * Метод активации пользователя с помощью кода
     * @param activeClientRequest данные для активации
     * @return сообщение о успешной активации
     */
    @Override
    public ClientRegistrationResponse putActiveUser(ActiveClientRequest activeClientRequest) {
        log.trace("UserServiceImpl.putActiveUser - activeClientRequest {}", activeClientRequest);
        Users user = userRepository.findFirstByEmail(activeClientRequest.email());

        if (user == null) {
            throw new EntityNotFoundException("Entity with email " + activeClientRequest.email() + " not found");
        } else if (user.getStatus() == StatusUserEnum.ACTIVE) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Activation error", "The account has already been activated!"));
            throw new InvalidDataException(errorMessages);
        }

        user.setStatus(StatusUserEnum.ACTIVE);
        userRepository.save(user);

        return new ClientRegistrationResponse("The account '" + user.getEmail() + "' is activated");
    }

    /**
     * Метод получения пользователя по почте
     * @param userEmail почта пользователя
     * @return данные пользователя
     */
    @Override
    public Users getUserByEmail(String userEmail) {
        log.trace("UserServiceImpl.getUserByEmail - userEmail {}", userEmail);
        Users user = userRepository.findFirstByEmail(userEmail);

        if (user == null) {
            throw new EntityNotFoundException("Entity with email " + userEmail + " not found");
        }

        return user;
    }

    /**
     * Метод получения пользователя организации по почте
     * @param userEmail почта пользователя
     * @return данные пользователя
     */
    @Override
    public Users getUserOrganizationByEmail(String userEmail) {
        log.trace("UserServiceImpl.getUserOrganizationByEmail - userEmail {}", userEmail);
        Users user = userRepository.findFirstByEmail(userEmail);

        if (user == null || user.getRole() != RoleEnum.ORGANIZATION) {
            throw new EntityNotFoundException("A broken token!");
        }

        return user;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, EntityNotFoundException {
        log.trace("UserServiceImpl.loadUserByUsername - email {}", email);

        Users user = userRepository.findFirstByEmail(email);

        if (user == null) {
            if (administratorService.getAdminByEmail(email) != null) {
                Administrator administrator = administratorService.getAdminByEmail(email);
                return new User(
                        administrator.getEmail(),
                        administrator.getPassword(),
                        Collections.singleton(administrator.getRole())
                );
            }
            throw new EntityNotFoundException("Entity with email " + email + " not found");
        }

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );
    }

}
