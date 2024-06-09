package biletka.main.service.Impl;

import biletka.main.dto.request.event_item.EventAdditional;
import biletka.main.entity.Actor;
import biletka.main.entity.Tag;
import biletka.main.entity.event_item.EventAdditionalInformation;
import biletka.main.repository.EventAdditionalInformationRepository;
import biletka.main.service.ActorService;
import biletka.main.service.EventAdditionalInformationService;
import biletka.main.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventAdditionalInformationServiceImpl implements EventAdditionalInformationService {
    private final EventAdditionalInformationRepository eventAdditionalInformationRepository;

    private final ActorService actorService;
    private final TagService tagService;

    /**
     * Метод создания и сохрания дополнительной ифнормации в бд
     * @param eventAdditional дополнительная информация
     * @return дополнительная информация мероприятия
     */
    @Override
    public EventAdditionalInformation createEventAdditional(EventAdditional eventAdditional) {
        log.trace("EventAdditionalInformationServiceImpl.createEventAdditional - eventAdditional {}", eventAdditional);
        /** Получение актёров */
        Set<Actor> actorSet = new HashSet<>();

        for (String actorRequest: eventAdditional.actors()) {
            Actor actor = actorService.getActorOfName(actorRequest);

            if (actor == null) {
                actor = actorService.createActor(actorRequest);
            }

            actorSet.add(actor);
        }

        /** Получения тегов меоприятия */
        Set<Tag> tagSet = new HashSet<>();

        for (String tagRequest: eventAdditional.tags()) {
            Tag tag = tagService.getTagOfName(tagRequest);

            if (tag == null) {
                tag = tagService.createTag(tagRequest);
            }

            tagSet.add(tag);
        }

        /** Сохранение дополнительной информации */
        EventAdditionalInformation eventAdditionalInformation = new EventAdditionalInformation(
                eventAdditional.author(),
                eventAdditional.director(),
                eventAdditional.writerOrArtist(),
                actorSet,
                tagSet
        );

        eventAdditionalInformationRepository.saveAndFlush(eventAdditionalInformation);

        return eventAdditionalInformation;
    }
}
