package biletka.main.service;

import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface AdministratorService {
    ArrayList<IpAddressMatcher> getMassiveIpAddress();
}
