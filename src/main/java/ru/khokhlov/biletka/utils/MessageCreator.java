package ru.khokhlov.biletka.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MessageCreator {
    @Value(value = "${application.server.address}")
    private String severAddress;
    public String createWelcomeMessage(String username, String entity, String link){
        return "Привет, " + username + "!\nДля подтверждения почты перейдите по ссылке: \n" + severAddress + entity + "/activate/" + link;
    }

    public String createHallMessage(Long id, String email, Long placeId, String name) {
        return "Id организации: " + id + "\nEmail организации: " + email + "\n\n Подтверждение схемы зала \"" + name + "\" (" + placeId + ")";
    }
}
