package biletka.main.Utils;

import biletka.main.dto.request.EventCreateRequest;
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

    public String convertToSymbolicString(String oldString) {
        log.trace("ConvertUtils.convertToSymbolicString - oldString {}", oldString);
        String newString = oldString.toLowerCase()
                .replaceAll("[^a-zа-я0-9!@\\s+]", "")
                .replaceAll(" ", "-")
                .replaceAll("-+", "-");

        return newString;
    }
}
