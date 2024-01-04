package ru.khokhlov.biletka.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageCreator {
    @Value(value = "${application.server.address}")
    private String severAddress;
    public String createWelcomeMessage(String username, String entity, String link){
        return "Привет, " + username + "!\nДля подтверждения почты перейдите по ссылке: \n" + severAddress + entity + "/activate/" + link;
    }
}
