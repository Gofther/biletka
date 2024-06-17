package biletka.main.service.Impl;

import biletka.main.Utils.ConvertUtils;
import biletka.main.Utils.FileUtils;
import biletka.main.Utils.JwtTokenUtils;
import biletka.main.Utils.PasswordEncoder;
import biletka.main.dto.request.AuthForm;
import biletka.main.dto.response.AuthResponse;
import biletka.main.dto.response.MessageCreateResponse;
import biletka.main.entity.Administrator;
import biletka.main.entity.Hall;
import biletka.main.enums.RoleEnum;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.AdministratorRepository;
import biletka.main.repository.HallRepository;
import biletka.main.service.AdministratorService;
import biletka.main.service.HallService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdministratorServiceImpl implements AdministratorService {
    private final AdministratorRepository administratorRepository;
    private final HallRepository hallRepository;
    private final HallService hallService;
    private final JwtTokenUtils jwtTokenUtils;
    private final FileUtils fileUtils;
    private final ConvertUtils convertUtils;

    /**
     * Метод аутентификации администратор
     * @param authForm форма аутентификации
     * @return токен для авторизации
     */
    @Override
    public AuthResponse postAuth(AuthForm authForm, HttpServletRequest request) {
        log.trace("AdministratorServiceImpl.postAuth - authForm {}", authForm);
        Administrator administrator = administratorRepository.findFirstByEmailAndAddress(authForm.email(), request.getRemoteAddr());

        if (administrator == null ||
                administrator.getStatus().getStatusUser().equalsIgnoreCase("INACTIVE") ||
                administrator.getStatus().getStatusUser().equalsIgnoreCase("ACTIVE") &&
                        (
                                !administrator.getRole().getAuthority().equalsIgnoreCase(authForm.role()) ||
                                        PasswordEncoder.arePasswordsEquals(administrator.getPassword(), authForm.password())
                        )
        ) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Authentication error", "The email or password is incorrect!"));
            throw new InvalidDataException(errorMessages);
        }
        // Проверка на бан аккаунта
        else if (administrator.getStatus().getStatusUser().equalsIgnoreCase("BANNED")) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Banned", "The account is banned!"));
            throw new InvalidDataException(errorMessages);
        }

        UserDetails userDetails = new User(
                administrator.getEmail(),
                administrator.getPassword(),
                Collections.singleton(administrator.getRole())
        );

        return new AuthResponse(
                jwtTokenUtils.generateToken(userDetails)
        );
    }

    @Override
    public MessageCreateResponse postHallScheme(String authorization, Long hallId, MultipartFile file, String scheme) throws IOException {
        String typeFile = fileUtils.getFileExtension(file.getOriginalFilename());

        fileUtils.validationFile(
                typeFile,
                new String[]{"svg"}
        );

        String userEmail = jwtTokenUtils.getUsernameFromToken(
                authorization.substring(7)
        );

        Administrator admin = getAdminByEmail(userEmail);

        if (admin == null) {
            throw new EntityNotFoundException("A broken token!");
        }

        Hall hall = hallService.getHallById(hallId);
        hall.setScheme(scheme);
        hallRepository.save(hall);

        fileUtils.fileUploadHall(file,file.getOriginalFilename());

        return new MessageCreateResponse(
                "File '" + file.getOriginalFilename() + "'and scheme has been saved for hall " + hallId
        );
    }
    @Override
    public Administrator getAdminByEmail(String email) {
        return administratorRepository.findFirstByEmail(email);
    }

    @Override
    public boolean checkAdminByIp(String request) {
        return administratorRepository.findFirstByAddress(request) != null;
    }

    @Override
    public Administrator getAdminByIpAndEmail(String usernameFromToken, String remoteAddr)  {
        Administrator administrator = administratorRepository.findFirstByEmailAndAddress(usernameFromToken, remoteAddr);

        if (administrator == null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Forbidden", "Access Denied"));
            throw new InvalidDataException(errorMessages);
        }

        return administrator;
    }
}
