package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.FavoriteWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteWordRepository extends JpaRepository<FavoriteWord, Long>  {
    boolean existsByUser_UserIdAndMyWordMnemonic_MyWordMnemonicId(Long userId, Long myWordMnemonicId);

    List<FavoriteWord> findAllByUser_UserIdAndMyWordMnemonic_MyWordMnemonicIdIn(Long userId, List<Long> ids);
}
