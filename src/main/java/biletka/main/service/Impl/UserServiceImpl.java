package biletka.main.service.Impl;

import biletka.main.entity.Users;
import biletka.main.repository.UserRepository;
import biletka.main.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Функция получения id пользователя по почте и паролю
     * @param email почта пользователя
     * @param password пароль пользователя
     * @return id пользователя
     */
    @Override
    public Long getClientIdByEmailAndPassword(String email, String password) {
        Users user = userRepository.findFirstByEmail(email);

        return user.getId();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, EntityNotFoundException {
        log.trace("UserServiceImpl.loadUserByUsername - username {}", email);

        Users user = userRepository.findFirstByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("Entity with email " + email + " not found");
        }

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(user.getRole())
        );
    }

}
