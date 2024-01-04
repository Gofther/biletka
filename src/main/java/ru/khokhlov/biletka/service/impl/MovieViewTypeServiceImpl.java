package ru.khokhlov.biletka.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.biletka.entity.MovieViewType;
import ru.khokhlov.biletka.repository.MovieViewTypeRepository;
import ru.khokhlov.biletka.service.MovieViewTypeService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieViewTypeServiceImpl implements MovieViewTypeService {
    private final MovieViewTypeRepository movieViewTypeRepository;

    @Override
    public MovieViewType createMovieViewType(String type) {
        log.trace("MovieViewTypeServiceImpl.createMovieViewType - type {}", type);

        MovieViewType movieViewType = new MovieViewType(type);

        movieViewTypeRepository.saveAndFlush(movieViewType);

        return movieViewType;
    }

    @Override
    public MovieViewType getFirstByType(String type) {
        log.trace("MovieViewTypeServiceImpl.getFirstByType - type {}", type);

        return movieViewTypeRepository.getFirstByMvtName(type);
    }
}
