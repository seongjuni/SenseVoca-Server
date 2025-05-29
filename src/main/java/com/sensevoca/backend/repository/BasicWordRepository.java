package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.BasicWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicWordRepository extends JpaRepository<BasicWord, Long> {
}
