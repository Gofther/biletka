package ru.khokhlov.biletka.service;

import ru.khokhlov.biletka.entity.Actor;

public interface ActorService {
    /**
     * Функция добавления актера по имени
     *
     * @param name имя
     */
    void createActor(String name);


    /**
     * Функция получения актера по имени
     *
     * @param name имя актера
     * @return актер
     */
    Actor getActorByName(String name);
}
