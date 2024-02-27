package ru.khokhlov.biletka.service.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ru.khokhlov.biletka.dto.request.HallCreationRequestDTO;
import ru.khokhlov.biletka.dto.response.HallCreationResponseDTO;
import ru.khokhlov.biletka.dto.response.SchemeClientResponse;
import ru.khokhlov.biletka.dto.response.SchemeResponse;
import ru.khokhlov.biletka.dto.response.scheme_full.SchemeFloor;
import ru.khokhlov.biletka.dto.response.scheme_full.SchemeRow;
import ru.khokhlov.biletka.dto.response.scheme_full.SchemeSeat;
import ru.khokhlov.biletka.entity.HallScheme;
import ru.khokhlov.biletka.entity.Organization;
import ru.khokhlov.biletka.entity.Place;
import ru.khokhlov.biletka.entity.Session;
import ru.khokhlov.biletka.repository.HallSchemeRepository;
import ru.khokhlov.biletka.repository.TicketRepository;
import ru.khokhlov.biletka.repository.TicketUserRepository;
import ru.khokhlov.biletka.service.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HallSchemeServiceImpl implements HallSchemeService {
    private final OrganizationService organizationService;
    private final PlaceService placeService;
    private final HallSchemeRepository hallSchemeRepository;
    private final SessionService sessionService;
    private final TicketRepository ticketRepository;
    private final TicketUserRepository ticketUserRepository;

    @Override
    public HallScheme getHallScheme(Long id) throws EntityNotFoundException {
        log.trace("HallSchemeServiceImpl.getHallScheme - id {}", id);
        return hallSchemeRepository.getReferenceById(id);
    }

    @Override
    public HallCreationResponseDTO createHallScheme(String city, HallCreationRequestDTO hallCreationRequestDTO) throws EntityNotFoundException {
        log.trace("HallSchemeServiceImpl.createHallScheme - city {}, HallCreationRequestDTO {}", city, hallCreationRequestDTO);

        Place place = placeService.getPlaceById(hallCreationRequestDTO.placeId());

        HallScheme hallScheme = new HallScheme(
                hallCreationRequestDTO.name(),
                hallCreationRequestDTO.info(),
                hallCreationRequestDTO.floor(),
                hallCreationRequestDTO.hallNumber(),
                hallCreationRequestDTO.seatsCount(),
                null,
                hallCreationRequestDTO.seatsGroupInfo(),
                place);

        try {
            hallSchemeRepository.saveAndFlush(hallScheme);
            Long hallId = hallScheme.getId();

            log.trace("HallSchemeServiceImpl.createHallScheme - HallId {}", hallId);
            return new HallCreationResponseDTO(hallId);
        }
        catch (RuntimeException ex){
            throw new EntityNotFoundException("Place with id " + hallCreationRequestDTO.placeId() + " not found");
        }
    }

    @Override
    public List<HallScheme> getAllHallByOrganization(String city, Long organizationId) throws EntityNotFoundException {
        log.trace("HallSchemeServiceImpl.getAllPlaceByOrganization - City {}, OrganizationId {}", city, organizationId);

        Organization organization = organizationService.getOrganizationById(organizationId);
        List<Place> placeList = new ArrayList<>(organization.getPlaceSet());
        List<HallScheme> hallSchemeList = new ArrayList<>();

        for (Place place : placeList) {
            hallSchemeList.addAll(hallSchemeRepository.findAllByPlace(place));
        }

        log.trace("HallSchemeServiceImpl.getAllPlaceByOrganization - List<HallScheme> {}", hallSchemeList);
        return hallSchemeList;
    }

    @Override
    public String putHall(String scheme, Long id) {
        HallScheme hallScheme = hallSchemeRepository.getReferenceById(id);

        if (hallScheme.getScheme() != null) {
            throw new EntityExistsException("Scheme in hall: " + hallScheme.getId() + " not exists");
        }

        hallScheme.setScheme(scheme);
        hallSchemeRepository.saveAndFlush(hallScheme);

        return hallScheme.getScheme();
    }

    @Override
    public SchemeResponse getScheme(Long hallId) {
        HallScheme hallScheme = hallSchemeRepository.getReferenceById(hallId);

        if (hallScheme.getScheme() == null) {
            throw new EntityExistsException("Scheme in hall: " + hallScheme.getId() + " already exists");
        }

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document doc;

        try {
            doc = builderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(hallScheme.getScheme())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Node rootNode = doc.getFirstChild();
        NodeList rootChilds = rootNode.getChildNodes();
        Integer numberFloor = 0;
        List<SchemeFloor> schemeFloors = new ArrayList<>();

        // Добавление информации о зале
        for (int floor=0; floor<rootChilds.getLength(); floor++) {
            if (rootChilds.item(floor).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            NodeList floorChilds = rootChilds.item(floor).getChildNodes();
            List<SchemeRow> schemeRows = new ArrayList<>();

            // Добавление информации о ряде
            for (int row=0; row<floorChilds.getLength(); row++) {
                if (floorChilds.item(row).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                String rowNumber = null;
                NodeList rowChilds = floorChilds.item(row).getChildNodes();

                // Добавление информации о элементах ряда
                for (int itemRow=0; itemRow<rowChilds.getLength(); itemRow++) {
                    if (rowChilds.item(itemRow).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    if (rowChilds.item(itemRow).getNodeName().equals("row-number")) {
                        rowNumber = rowChilds.item(itemRow).getTextContent();
                        continue;
                    }

                    NodeList seatChilds = rowChilds.item(itemRow).getChildNodes();
                    List<SchemeSeat> schemeSeats = new ArrayList<>();

                    // Добавление информации о месте
                    for (int seat=0; seat<seatChilds.getLength(); seat++) {
                        if (seatChilds.item(seat).getNodeType() != Node.ELEMENT_NODE) {
                            continue;
                        }

                        Boolean occupied = false;
                        String number = "";
                        String group = "";
                        String position = "";
                        NodeList itemSeatChilds = seatChilds.item(seat).getChildNodes();

                        for (int seatItem=0; seatItem<itemSeatChilds.getLength(); seatItem++) {
                            if (itemSeatChilds.item(seatItem).getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }

                            if (itemSeatChilds.item(seatItem).getNodeName().equals("seat-occupied")) {
                                continue;
                            }

                            if (itemSeatChilds.item(seatItem).getNodeName().equals("seat-number")) {
                                number = itemSeatChilds.item(seatItem).getTextContent();
                            } else if (itemSeatChilds.item(seatItem).getNodeName().equals("seat-group")) {
                                group = itemSeatChilds.item(seatItem).getTextContent();
                            } else {
                                position = itemSeatChilds.item(seatItem).getTextContent();
                            }
                        }

                        SchemeSeat schemeSeat = new SchemeSeat(
                                false,
                                number,
                                group,
                                position
                        );
                        schemeSeats.add(schemeSeat);
                    }
                    // Добавление информации о месте

                    SchemeRow schemeRow = new SchemeRow(
                            rowNumber,
                            schemeSeats.toArray(SchemeSeat[]::new)
                    );
                    schemeRows.add(schemeRow);
                }
                // Добавление информации о элементах ряда
            }
            // Добавление информации о ряде

            schemeFloors.add(new SchemeFloor(
                    ++numberFloor,
                    schemeRows.toArray(SchemeRow[]::new)
            ));
        }
        // Добавление информации о зале

        return new SchemeResponse(schemeFloors.toArray(SchemeFloor[]::new));
    }

    @Override
    public boolean getSeatScheme(Long id, Integer rowNumber, Integer seatNumber) {
        HallScheme hallScheme = hallSchemeRepository.getReferenceById(id);

        if (hallScheme.getScheme() == null) {
            throw new EntityExistsException("Scheme in hall: " + hallScheme.getId() + " already exists");
        }

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document doc;

        try {
            doc = builderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(hallScheme.getScheme())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Node rootNode = doc.getFirstChild();
        NodeList rootChilds = rootNode.getChildNodes();
        List<SchemeFloor> schemeFloors = new ArrayList<>();

        for (int floor=0; floor<rootChilds.getLength(); floor++) {
            if (rootChilds.item(floor).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            NodeList floorChilds = rootChilds.item(floor).getChildNodes();

            for (int floorChild=0; floorChild<floorChilds.getLength(); floorChild++) {
                if (floorChilds.item(floorChild).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                boolean rowTrue = false;
                NodeList rowChilds = floorChilds.item(floorChild).getChildNodes();

                for (int rowChild=0; rowChild<rowChilds.getLength(); rowChild++) {
                    if (rowChilds.item(rowChild).getNodeType()!= Node.ELEMENT_NODE ) {
                        continue;
                    }

                    if (rowChilds.item(rowChild).getNodeName() == "row-number" && Integer.valueOf(rowChilds.item(rowChild).getTextContent()) == rowNumber) {
                        rowTrue = true;
                        continue;
                    }

                    if (rowTrue) {
                        NodeList seats = rowChilds.item(rowChild).getChildNodes();

                        for (int seat=0; seat<seats.getLength(); seat++) {
                            if (seats.item(seat).getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }

                            NodeList seatItem = seats.item(seat).getChildNodes();

                            for (int item=0; item<seatItem.getLength(); item++) {
                                if (seatItem.item(item).getNodeType()!= Node.ELEMENT_NODE) {
                                    continue;
                                }

                                if (seatItem.item(item).getNodeName().equals("seat-number") &&
                                        seatNumber == Integer.valueOf(seatItem.item(item).getTextContent())) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public SchemeClientResponse getSchemeBySession(Long sessionId) {
        Session session = sessionService.getSessionById(sessionId);

        if (session.getRoomLayout().getScheme() == null) {
            throw new EntityExistsException("Scheme in hall: " + session.getRoomLayout().getId() + " already exists");
        }

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document doc;

        try {
            doc = builderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(session.getRoomLayout().getScheme())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Node rootNode = doc.getFirstChild();
        NodeList rootChilds = rootNode.getChildNodes();
        Integer numberFloor = 0;
        List<SchemeFloor> schemeFloors = new ArrayList<>();

        // Добавление информации о зале
        for (int floor=0; floor<rootChilds.getLength(); floor++) {
            if (rootChilds.item(floor).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            NodeList floorChilds = rootChilds.item(floor).getChildNodes();
            List<SchemeRow> schemeRows = new ArrayList<>();

            // Добавление информации о ряде
            for (int row=0; row<floorChilds.getLength(); row++) {
                if (floorChilds.item(row).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                String rowNumber = null;
                NodeList rowChilds = floorChilds.item(row).getChildNodes();

                // Добавление информации о элементах ряда
                for (int itemRow=0; itemRow<rowChilds.getLength(); itemRow++) {
                    if (rowChilds.item(itemRow).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    if (rowChilds.item(itemRow).getNodeName().equals("row-number")) {
                        rowNumber = rowChilds.item(itemRow).getTextContent();
                        continue;
                    }

                    NodeList seatChilds = rowChilds.item(itemRow).getChildNodes();
                    List<SchemeSeat> schemeSeats = new ArrayList<>();

                    // Добавление информации о месте
                    for (int seat=0; seat<seatChilds.getLength(); seat++) {
                        if (seatChilds.item(seat).getNodeType() != Node.ELEMENT_NODE) {
                            continue;
                        }

                        boolean occupied = false;
                        String number = "";
                        String group = "";
                        String position = "";
                        NodeList itemSeatChilds = seatChilds.item(seat).getChildNodes();

                        for (int seatItem=0; seatItem<itemSeatChilds.getLength(); seatItem++) {
                            if (itemSeatChilds.item(seatItem).getNodeType() != Node.ELEMENT_NODE) {
                                continue;
                            }

                            if (itemSeatChilds.item(seatItem).getNodeName().equals("seat-number")) {
                                number = itemSeatChilds.item(seatItem).getTextContent();
                            } else if (itemSeatChilds.item(seatItem).getNodeName().equals("seat-group")) {
                                group = itemSeatChilds.item(seatItem).getTextContent();
                            } else if (itemSeatChilds.item(seatItem).getNodeName().equals("seat-position")) {
                                position = itemSeatChilds.item(seatItem).getTextContent();
                            } else if (itemSeatChilds.item(seatItem).getNodeName().equals("seat-occupied")) {
                                occupied = ticketUserRepository.getFirstBySessionAndRowAndSeat(sessionId, Integer.valueOf(rowNumber), Integer.valueOf(number)) != null;
                            }
                        }

                        SchemeSeat schemeSeat = new SchemeSeat(
                                occupied,
                                number,
                                group,
                                position
                        );

                        schemeSeats.add(schemeSeat);
                    }
                    // Добавление информации о месте

                    SchemeRow schemeRow = new SchemeRow(
                            rowNumber,
                            schemeSeats.toArray(SchemeSeat[]::new)
                    );
                    schemeRows.add(schemeRow);
                }
                // Добавление информации о элементах ряда
            }
            // Добавление информации о ряде

            schemeFloors.add(new SchemeFloor(
                    ++numberFloor,
                    schemeRows.toArray(SchemeRow[]::new)
            ));
        }
        // Добавление информации о зале

        return new SchemeClientResponse(
                schemeFloors.toArray(SchemeFloor[]::new),
                session.getRoomLayout().getSeatGroupInfo(),
                ticketRepository.getTicketsBySession(session.getId()).getPrice()
        );
    }

    @Override
    public String getPriceByRowAndSeat(HallScheme hallScheme, Integer row, Integer seat) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        Document doc;

        try {
            doc = builderFactory.newDocumentBuilder().parse(new InputSource(new StringReader(hallScheme.getScheme())));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Node rootNode = doc.getFirstChild();
        NodeList rootChilds = rootNode.getChildNodes();

        for (int floor=0; floor<rootChilds.getLength(); floor++) {
            if (rootChilds.item(floor).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            NodeList floorChilds = rootChilds.item(floor).getChildNodes();

            for (int rowItem=0; rowItem<floorChilds.getLength(); rowItem++) {
                if (floorChilds.item(rowItem).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                boolean rowTrue = false;
                NodeList rowChilds = floorChilds.item(rowItem).getChildNodes();

                for (int itemRow=0; itemRow<rowChilds.getLength(); itemRow++) {
                    if (rowChilds.item(itemRow).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    if (rowChilds.item(itemRow).getNodeName().equals("row-number") && Integer.valueOf(rowChilds.item(itemRow).getTextContent()) == row) {
                        rowTrue = true;
                        continue;
                    }

                    NodeList seatChilds = rowChilds.item(itemRow).getChildNodes();

                    for (int seatItem=0; seatItem<seatChilds.getLength(); seatItem++) {
                        if (seatChilds.item(seatItem).getNodeType() != Node.ELEMENT_NODE) {
                            continue;
                        }

                        boolean seatTrue = false;
                        NodeList itemSeatChilds = seatChilds.item(seatItem).getChildNodes();

                        for (int item=0; item<itemSeatChilds.getLength(); item++) {
                            if (itemSeatChilds.item(item).getNodeName().equals("seat-number") &&
                                    Integer.valueOf(itemSeatChilds.item(item).getTextContent()) == seat &&
                                    rowTrue) {
                                seatTrue = true;
                            }

                            if (itemSeatChilds.item(item).getNodeName().equals("seat-group") &&
                                    rowTrue &&
                                    seatTrue) {
                                return itemSeatChilds.item(item).getTextContent();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
