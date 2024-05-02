package biletka.main.service;

import biletka.main.entity.Type_event;
import org.springframework.stereotype.Service;

@Service
public interface Type_eventService {
    Type_event getType_eventOfId(Long id);

    Type_event getType_eventOfName(String type);
}
