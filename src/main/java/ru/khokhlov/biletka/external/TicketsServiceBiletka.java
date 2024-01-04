package ru.khokhlov.biletka.external;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "tickets", url = "${pushkin.server.url.tickets.test}")
public interface TicketsServiceBiletka {
   // @PostMapping(value = "/tickets")
}
