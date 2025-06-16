package com.sensevoca.backend.repository;

import com.sensevoca.backend.domain.BasicWord;
import com.sensevoca.backend.domain.FavoriteWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteWordRepository extends JpaRepository<FavoriteWord, Long>  {
    boolean existsByUser_UserIdAndMyWordMnemonic_MyWordMnemonicId(Long userId, Long myWordMnemonicId);

    List<FavoriteWord> findAllByUser_UserIdAndMyWordMnemonic_MyWordMnemonicIdIn(Long userId, List<Long> ids);

    List<FavoriteWord> findAllByUser_UserIdOrderByCreatedAtAsc(Long userId);

    Optional<FavoriteWord> findByUser_UserIdAndMyWordMnemonic_MyWordMnemonicId(Long userId, Long myWordMnemonicId);

    // 기본 단어장
    // 1) user_id가 basic_word를 즐겨찾기 했는지 여부
    boolean existsByUser_UserIdAndDayword_DaywordId(Long userId, Long daywordId);
    // 2) basic_word에서 즐겨찾기 여부 판단
    List<FavoriteWord> findAllByUser_UserIdAndDayword_DaywordIdIn(Long userId, List<Long> daywordIdList);
    // 3) user_id가 갖고 있는 하나의 basic_word_id 조회 (삭제용)
    Optional<FavoriteWord> findByUser_UserIdAndDayword_DaywordId(Long userId, Long daywordId);

}
