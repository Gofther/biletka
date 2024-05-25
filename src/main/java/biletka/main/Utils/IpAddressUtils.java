package biletka.main.Utils;

import biletka.main.entity.Administrator;
import biletka.main.enums.RoleEnum;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.service.AdministratorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class IpAddressUtils {
    private final AdministratorService administratorService;
    private final JwtTokenUtils jwtTokenUtils;

    public boolean checkIpAdministrator(String request) {
        return administratorService.checkAdminByIp(request);
    }

    public void checkIpInAdministrator(HttpServletRequest request, String authorization) {
        Administrator administrator = administratorService.getAdminByIpAndEmail(
                jwtTokenUtils.getUsernameFromToken(authorization.substring(7)),
                request.getRemoteAddr()
        );

        if (administrator.getRole() != RoleEnum.ADMIN) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Forbidden", "Access Denied"));
            throw new InvalidDataException(errorMessages);
        }
    }
}
