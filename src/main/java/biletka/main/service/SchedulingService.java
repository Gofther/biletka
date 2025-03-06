package biletka.main.service;

import biletka.main.entity.Organization;

public interface SchedulingService {
    String getFileWithTicketsInfo(Organization organization);
}
