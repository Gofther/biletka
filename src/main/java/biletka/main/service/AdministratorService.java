package biletka.main.service;

import biletka.main.dto.request.AuthForm;
import biletka.main.dto.response.AuthResponse;
import biletka.main.entity.Administrator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface AdministratorService {

    /**
     * Метод аутентификации администратор
     * @param authForm форма аутентификации
     * @return токен для авторизации
     */
    AuthResponse postAuth(AuthForm authForm, HttpServletRequest request);

    Administrator getAdminByEmail(String email);

    boolean checkAdminByIp(String request);

    Administrator getAdminByIpAndEmail(String usernameFromToken, String remoteAddr);
}
