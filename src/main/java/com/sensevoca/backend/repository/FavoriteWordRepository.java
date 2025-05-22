package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.FavoriteWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteWordRepository extends JpaRepository<FavoriteWord, Long>  {
    boolean existsByUserIdAndMyWordMnemonicId(Long userId, Long myWordMnemonicId);
}
