package biletka.main.Utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ActivationCode {
    private Random random = new Random();

    /**
     * Метод генерации случайной строки 0-9 и A-Z размером 5 символов
     * @return строка кода активации
     */
    public String generateActivationCode() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 90; // letter 'Z'
        int targetStringLength = 5;

        return random.ints(leftLimit, rightLimit + 1)
                .filter(el -> el <= 57 || el >= 65)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
