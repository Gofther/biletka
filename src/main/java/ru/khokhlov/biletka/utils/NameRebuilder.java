package ru.khokhlov.biletka.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NameRebuilder {

    /**
     * Функция преобразования имени события
     * Пример: Spider-Man: No way to home -> spider-man-no-way-home
     *
     * @param name естественное название события
     * @return измененное имя события
     */
    public static String rebuildName(String name) {
        log.debug("BasicInformationServiceImpl.rebuildName - String {}", name);

        String slug = name.toLowerCase(); // Преобразование к нижнему регистру
        slug = slug.replaceAll("[^a-zA-Zа-яА-Я0-9\\s-]", ""); // Удаление символов, кроме букв, цифр, пробелов и дефисов
        slug = slug.replaceAll("\\s+", "-"); // Замена пробелов на дефисы
        slug = slug.replaceAll("-+", "-"); // Удаление повторяющихся дефисов

        log.debug("BasicInformationServiceImpl.rebuildName - rebuild name {}", name);
        return slug;
    }
}
