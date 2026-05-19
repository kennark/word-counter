package org.example.demo.common.repository;

import org.example.demo.common.entity.WordCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WordCountRepository extends JpaRepository<WordCount, Long> {
    List<WordCount> findAllByTextIdEquals(UUID textId);

    List<WordCount> findAllByTextIdEqualsAndCountGreaterThan(UUID id, int i);
}
