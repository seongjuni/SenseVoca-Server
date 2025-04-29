package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.MyWord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyWordRepository extends JpaRepository<MyWord, Long> {

}
