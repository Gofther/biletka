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
import ru.khokhlov.biletka.dto.response.SchemeResponse;
import ru.khokhlov.biletka.dto.response.scheme_full.SchemeFloor;
import ru.khokhlov.biletka.dto.response.scheme_full.SchemeRow;
import ru.khokhlov.biletka.dto.response.scheme_full.SchemeSeat;
import ru.khokhlov.biletka.entity.HallScheme;
import ru.khokhlov.biletka.entity.Organization;
import ru.khokhlov.biletka.entity.Place;
import ru.khokhlov.biletka.repository.HallSchemeRepository;
import ru.khokhlov.biletka.service.HallSchemeService;
import ru.khokhlov.biletka.service.OrganizationService;
import ru.khokhlov.biletka.service.PlaceService;

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

        //TODO Добавление информации о зале
        for (int floor=0; floor<rootChilds.getLength(); floor++) {
            if (rootChilds.item(floor).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            NodeList floorChilds = rootChilds.item(floor).getChildNodes();
            List<SchemeRow> schemeRows = new ArrayList<>();

            //TODO Добавление информации о ряде
            for (int row=0; row<floorChilds.getLength(); row++) {
                if (floorChilds.item(row).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                String rowNumber = null;
                NodeList rowChilds = floorChilds.item(row).getChildNodes();

                //TODO Добавление информации о элементах ряда
                for (int itemRow=0; itemRow<rowChilds.getLength(); itemRow++) {
                    if (rowChilds.item(itemRow).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }

                    if (rowChilds.item(itemRow).getNodeName() == "row-number") {
                        rowNumber = rowChilds.item(itemRow).getTextContent();
                        continue;
                    }

                    NodeList seatChilds = rowChilds.item(itemRow).getChildNodes();
                    List<SchemeSeat> schemeSeats = new ArrayList<>();

                    //TODO Добавление информации о месте
                    for (int seat=0; seat<seatChilds.getLength(); seat++) {
                        if (seatChilds.item(seat).getNodeType() != Node.ELEMENT_NODE) {
                            continue;
                        }

                        NodeList itemSeatChilds = seatChilds.item(seat).getChildNodes();
                        SchemeSeat schemeSeat = new SchemeSeat(
                                itemSeatChilds.item(2).getTextContent(),
                                itemSeatChilds.item(5).getTextContent(),
                                itemSeatChilds.item(8).getTextContent(),
                                itemSeatChilds.item(11).getTextContent()
                        );
                        schemeSeats.add(schemeSeat);
                    }
                    //TODO Добавление информации о месте

                    SchemeRow schemeRow = new SchemeRow(
                            rowNumber,
                            schemeSeats.toArray(SchemeSeat[]::new)
                    );
                    schemeRows.add(schemeRow);
                }
                //TODO Добавление информации о элементах ряда
            }
            //TODO Добавление информации о ряде

            schemeFloors.add(new SchemeFloor(
                    ++numberFloor,
                    schemeRows.toArray(SchemeRow[]::new)
            ));
        }
        //TODO Добавление информации о зале

        return new SchemeResponse(schemeFloors.toArray(SchemeFloor[]::new));
    }
}
