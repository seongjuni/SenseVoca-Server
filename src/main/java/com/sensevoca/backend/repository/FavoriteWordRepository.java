package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.FavoriteWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteWordRepository extends JpaRepository<FavoriteWord, Long>  {
    boolean existsByUser_UserIdAndMyWordMnemonic_MyWordMnemonicId(Long userId, Long myWordMnemonicId);

    List<FavoriteWord> findAllByUser_UserIdAndMyWordMnemonic_MyWordMnemonicIdIn(Long userId, List<Long> ids);

    List<FavoriteWord> findAllByUser_UserId(Long userId);

    Optional<FavoriteWord> findByUser_UserIdAndMyWordMnemonic_MyWordMnemonicId(Long userId, Long myWordMnemonicId);
}
