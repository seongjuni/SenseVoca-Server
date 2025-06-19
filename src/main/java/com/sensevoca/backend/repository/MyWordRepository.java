package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.MyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyWordRepository extends JpaRepository<MyWord, Long> {

    List<MyWord> findAllByMyWordbookMyWordbookId(Long wordbookId);
    List<MyWord> findAllByMyWordMnemonic_MyWordMnemonicIdAndMyWordbook_User_UserId(Long wordId, Long userId);
}
