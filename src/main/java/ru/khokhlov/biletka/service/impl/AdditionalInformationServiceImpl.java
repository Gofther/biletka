package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.dto.request.event_full_ei.AdditionalInformationRequest;
import ru.khokhlov.biletka.entity.Actor;
import ru.khokhlov.biletka.entity.EventAdditionalInformation;
import ru.khokhlov.biletka.entity.Tag;
import ru.khokhlov.biletka.repository.AdditionalInformationRepository;
import ru.khokhlov.biletka.service.ActorService;
import ru.khokhlov.biletka.service.AdditionalInformationService;
import ru.khokhlov.biletka.service.TagService;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdditionalInformationServiceImpl implements AdditionalInformationService {
    private final AdditionalInformationRepository additionalInformationRepository;
    private final TagService tagService;
    private final ActorService actorService;

    @Override
    public EventAdditionalInformation createAdditionalInformation(AdditionalInformationRequest additionalInformationRequest) {
        log.trace("AdditionalInformationServiceImpl.createAdditionalInformation - additionalInformationRequest {}", additionalInformationRequest);

        EventAdditionalInformation eventAdditionalInformation = new EventAdditionalInformation(
                additionalInformationRequest.director(),
                additionalInformationRequest.writerOrArtist(),
                additionalInformationRequest.author()
        );

        Set<Tag> tagSet = new HashSet<>();
        for (String tag:additionalInformationRequest.tags()) {
            if (tagService.getTagByName(tag) == null)
                tagService.createTag(tag);
            tagSet.add(tagService.getTagByName(tag));
        }
        eventAdditionalInformation.setTags(tagSet);

        Set<Actor> actorSet = new HashSet<>();
        for (String tag:additionalInformationRequest.tags()) {
            if (actorService.getActorByName(tag) == null)
                actorService.createActor(tag);
            actorSet.add(actorService.getActorByName(tag));
        }
        eventAdditionalInformation.setActors(actorSet);

        additionalInformationRepository.saveAndFlush(eventAdditionalInformation);

        return eventAdditionalInformation;
    }

    @Override
    public void deleteAdditionalInformation(EventAdditionalInformation eventAdditionalInformation){
        additionalInformationRepository.delete(eventAdditionalInformation);
    }
}
