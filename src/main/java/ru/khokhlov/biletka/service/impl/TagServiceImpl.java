package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.Tag;
import ru.khokhlov.biletka.repository.TagRepository;
import ru.khokhlov.biletka.service.TagService;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public void createTag(String name) {
        log.trace("TagServiceImpl.createTag - name {}", name);

        Tag tag = new Tag(name);

        tagRepository.saveAndFlush(tag);
    }

    @Override
    public Tag getTagByName(String name) {
        log.trace("TagServiceImpl.getTagByName - name {}", name);

        return tagRepository.findFirstByName(name);
    }
}
