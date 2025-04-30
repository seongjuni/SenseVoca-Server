package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.MyWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyWordRepository extends JpaRepository<MyWord, Long> {
    List<MyWord> findAllByWordbookId(Long wordbookId);
}
