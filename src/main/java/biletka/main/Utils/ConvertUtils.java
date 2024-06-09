package biletka.main.Utils;

import biletka.main.dto.request.EventCreateRequest;
import biletka.main.dto.request.HallCreateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConvertUtils {
    /**
     * Метод конвертации строки запроса в JSON
     * @param stringObject строка запроса
     * @return JSON объект
     */
    public EventCreateRequest convertToJSONEventCreate(String stringObject) throws JsonProcessingException {
        log.trace("ConvertUtils.convertToJSONEventCreate - stringObject {}", stringObject);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(stringObject, EventCreateRequest.class);
    }

    /**
     * Метод конвертации строки запроса в JSON
     * @param stringObject строка запроса
     * @return JSON объект
     */
    public HallCreateRequest convertToJSONHallCreate(String stringObject) throws JsonProcessingException {
        log.trace("ConvertUtils.convertToJSONHallCreate - stringObject {}", stringObject);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(stringObject, HallCreateRequest.class);
    }

    /**
     * Метод преобразования строки в символичныую без лишних знаков
     * @param oldString строка для преобразования
     * @return строка без лишних символов
     */
    public String convertToSymbolicString(String oldString) {
        log.trace("ConvertUtils.convertToSymbolicString - oldString {}", oldString);
        String newString = oldString.toLowerCase()
                .replaceAll("[^a-zа-я0-9!@\\s+]", "")
                .replaceAll(" ", "-")
                .replaceAll("-+", "-");

        return newString;
    }
}
