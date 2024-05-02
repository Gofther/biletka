package biletka.main.service;

import biletka.main.entity.Tag;
import org.springframework.stereotype.Service;

@Service
public interface TagService {
    Tag getTagOfId(Long id);

    Tag getTagOfName(String name);
}
