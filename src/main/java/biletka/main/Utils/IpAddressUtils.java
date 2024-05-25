package biletka.main.Utils;

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

    public boolean checkIpAdministrator(String request) {
        return administratorService.checkAdminByIp(request);
    }
}
