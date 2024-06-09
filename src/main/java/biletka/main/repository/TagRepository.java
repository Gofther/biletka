package biletka.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import biletka.main.entity.Tag;
public interface TagRepository extends JpaRepository<Tag, Long>{
    Tag findFirstByName(String name);
}
