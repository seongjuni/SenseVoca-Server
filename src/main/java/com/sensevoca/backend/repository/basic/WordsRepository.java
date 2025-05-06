package com.sensevoca.backend.repository.basic;

import com.sensevoca.backend.entity.basic.MnemonicExample;
import com.sensevoca.backend.entity.basic.Words;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordsRepository extends JpaRepository<Words, Long> {

}
