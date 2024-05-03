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
    /**
     * Метод Создания нового тага и сохранения в бд
     * @param name - имя актёра
     * @return Созданный актёр
     */
    @Override
    public Tag PostNewTag(String name){
        Tag tag = tagRepository.findFirstByName(name);
        if(tag!= null){
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage("Post error", "This actor already exists!"));
            throw new InvalidDataException(errorMessages);
        }
        Tag newTag = new Tag(name);
        tagRepository.saveAndFlush(newTag);
        return newTag;
    }
}
