package biletka.main.service;

import biletka.main.entity.TypeEvent;
import org.springframework.stereotype.Service;

@Service
public interface TypeEventService {
    TypeEvent getType_eventOfId(Long id);

    TypeEvent getType_eventOfName(String type);
}
