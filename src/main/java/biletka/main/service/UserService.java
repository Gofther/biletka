package biletka.main.service;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    Long getClientIdByEmailAndPassword(String email, String password);
}
