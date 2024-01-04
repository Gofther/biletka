package ru.khokhlov.biletka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.biletka.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findFirstByName(String name);
}
