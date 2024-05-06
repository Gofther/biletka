package biletka.main.service.Impl;

import biletka.main.entity.Tag;
import biletka.main.exception.ErrorMessage;
import biletka.main.exception.InvalidDataException;
import biletka.main.repository.TagRepository;
import biletka.main.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    /**
     * Метод получения тега мероприятия
     * @param name название тега
     * @return тег
     */
    @Override
    public Tag getTagOfName(String name){
        log.trace("TagServiceImpl.getTagOfName - name {}", name);
        return tagRepository.findFirstByName(name);
    }

    /**
     * Метод создания тега
     * @param name название тега
     * @return тег
     */
    @Override
    public Tag createTag(String name) {
        log.trace("TagServiceImpl.createTag - name {}", name);
        Tag tag = tagRepository.findFirstByName(name);

        if (tag != null) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Tag error", "This tag already exists!"));
            throw new InvalidDataException(errorMessages);
        }

        tag = new Tag(name);

        tagRepository.saveAndFlush(tag);

        return tag;
    }
}
