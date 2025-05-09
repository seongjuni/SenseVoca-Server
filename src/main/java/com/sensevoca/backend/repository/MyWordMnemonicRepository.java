package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.MyWord;
import com.sensevoca.backend.domain.MyWordMnemonic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyWordMnemonicRepository extends JpaRepository<MyWordMnemonic, Long> {
    Optional<MyWordMnemonic> findByWordInfoWordIdAndInterestInterestIdAndMeaning(Long wordId, Long interestId, String meaning);
}
