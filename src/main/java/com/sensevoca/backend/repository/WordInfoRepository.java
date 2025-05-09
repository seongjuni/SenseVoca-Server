package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.WordInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordInfoRepository extends JpaRepository<WordInfo, Long> {
    Optional<WordInfo> findByWordAndPhoneticUsAndPhoneticUkAndPhoneticAus(String word, String phoneticUs, String phoneticUk, String phoneticAus);
}
