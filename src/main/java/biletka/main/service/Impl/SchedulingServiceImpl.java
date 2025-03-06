package biletka.main.service.Impl;


import biletka.main.entity.*;
import biletka.main.repository.SessionRepository;
import biletka.main.repository.TicketRepository;
import biletka.main.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulingServiceImpl implements SchedulingService {
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;

    /**
     * Метод получения названия файла со статистикой по билетам
     *
     * @param organization организация
     * @return название файла
     */
    @Override
    public String getFileWithTicketsInfo(Organization organization) {
        log.trace("SchedulingServiceImpl.getFileWithTicketsInfo - organization {}", organization);
        Integer ticketCount = 0;
        Integer cancelCount = 0;
        Integer extinguishedCount = 0;
        double earnings = 0;
        String file = "TicketsInfo.doc";

        Set<Event> events = organization.getEventSet();
        if (!events.isEmpty()) {
            for (Event event : events) {
                ArrayList<Session> sessions = sessionRepository.findAllSessionByEvent(event);
                for (Session session : sessions) {
                    List<Ticket> tickets = ticketRepository.getAllBySession(session.getId());
                    for (Ticket ticket : tickets) {
                        Cheque cheque = ticket.getCheque();
                        ticketCount++;

                        if (cheque.getStatus() == Cheque.Status.CANCEL && cheque.getDate() == LocalDate.now())
                            cancelCount++;

                        if (ticket.getIsExtinguished()) extinguishedCount++;

                        if (cheque.getStatus() == Cheque.Status.BUY && ticket.getIsBought() && cheque.getDate() == LocalDate.now())
                            earnings += ticket.getPrice();
                    }
                }
            }

            //запись в файл
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write("Общее количество билетов: " + String.valueOf(ticketCount) + '\n');
                writer.write("Количество возвратов за день: " + cancelCount.toString() + '\n');
                writer.write("Количество посещений: " + extinguishedCount.toString() + '\n');
                writer.write("Возможная выручка за продажу биллетов за день: " + String.valueOf(earnings) + '\n');
                writer.flush();
            } catch (IOException ex) {

                System.out.println(ex.getMessage());
            }
        }
        return file;
    }
}