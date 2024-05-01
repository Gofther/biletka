package biletka.main.service.Impl;

import biletka.main.entity.Tag;
import biletka.main.repository.TagRepository;
import biletka.main.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public Tag getTagOfId(Long id){
        Tag tag = tagRepository.getReferenceById(Long.valueOf(id));
        return tag;
    }

    @Override
    public Tag getTagOfName(String name){
        Tag tag = tagRepository.findFirstByName(name);
        return tag;
    }
}
