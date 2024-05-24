package biletka.main.service.Impl;

import biletka.main.repository.AdministratorRepository;
import biletka.main.service.AdministratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdministratorServiceImpl implements AdministratorService {
    private final AdministratorRepository administratorRepository;

    @Override
    public ArrayList<IpAddressMatcher> getMassiveIpAddress() {
        log.trace("AdministratorServiceImpl.getMassiveIpAddress");
        ArrayList<IpAddressMatcher> ipAddressMatchers = new ArrayList<>();
        administratorRepository.getAllIp().forEach(ip -> {
            ipAddressMatchers.add(new IpAddressMatcher(ip));
        });
        return ipAddressMatchers;
    }
}
