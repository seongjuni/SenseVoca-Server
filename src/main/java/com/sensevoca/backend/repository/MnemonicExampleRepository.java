package com.sensevoca.backend.repository;

import com.sensevoca.backend.entity.MnemonicExample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MnemonicExampleRepository extends JpaRepository<MnemonicExample, Long> {
    Optional<MnemonicExample> findByWordAndInterestIdAndMeaning(String word, Long interestId, String meaning);
//    List<MnemonicExample> findAllByWordAndInterestId(String word, Long interestId);
}
