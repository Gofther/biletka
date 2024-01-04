package ru.khokhlov.biletka.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.Actor;
import ru.khokhlov.biletka.repository.ActorRepository;
import ru.khokhlov.biletka.service.ActorService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;

    @Override
    public void createActor(String name) {
        log.trace("ActorServiceImpl.createActor - name {}", name);

        Actor actor = new Actor(name);

        actorRepository.saveAndFlush(actor);
    }
    @Override
    public Actor getActorByName(String name) throws EntityNotFoundException {
        return actorRepository.findFirstByName(name);
    }
}
