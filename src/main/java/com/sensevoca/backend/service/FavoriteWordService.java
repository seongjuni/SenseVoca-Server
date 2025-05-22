package com.sensevoca.backend.service;

import com.sensevoca.backend.domain.FavoriteWord;
import com.sensevoca.backend.domain.MyWordMnemonic;
import com.sensevoca.backend.domain.User;
import com.sensevoca.backend.domain.WordInfo;
import com.sensevoca.backend.dto.favoriteword.GetFavoriteWordsResponse;
import com.sensevoca.backend.repository.FavoriteWordRepository;
import com.sensevoca.backend.repository.MyWordMnemonicRepository;
import com.sensevoca.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteWordService {

    private final FavoriteWordRepository favoriteWordRepository;
    private final UserRepository userRepository;
    private final MyWordMnemonicRepository myWordMnemonicRepository;

    public List<GetFavoriteWordsResponse> getFavoriteWordsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        List<FavoriteWord> favorites = favoriteWordRepository.findAllByUser_UserId(userId);

        return favorites.stream()
                .map(fav -> {
                    if (fav.getMyWordMnemonic() != null) {
                        WordInfo wordInfo = fav.getMyWordMnemonic().getWordInfo();
                        return GetFavoriteWordsResponse.builder()
                                .wordId(fav.getMyWordMnemonic().getMyWordMnemonicId())
                                .word(wordInfo.getWord())
                                .meaning(fav.getMyWordMnemonic().getMeaning())
                                .type("MY")
                                .build();
                    } else {
                        WordInfo wordInfo = fav.getBasicWord().getWordInfo();
                        return GetFavoriteWordsResponse.builder()
                                .wordId(fav.getBasicWord().getBasicWordId())
                                .word(wordInfo.getWord())
                                .meaning(fav.getBasicWord().getMeaning())
                                .type("BASIC")
                                .build();
                    }
                })
                .toList();
    }

    public void addMyWordFavorite(Long myWordMnemonicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 2. 니모닉 조회
        MyWordMnemonic myWordMnemonic = myWordMnemonicRepository.findById(myWordMnemonicId)
                .orElseThrow(() -> new IllegalArgumentException("해당 나만의 단어가 존재하지 않습니다."));

        if (favoriteWordRepository.existsByUser_UserIdAndMyWordMnemonic_MyWordMnemonicId(userId, myWordMnemonicId)) {
            throw new IllegalStateException("이미 즐겨찾기 되어 있음");
        }

        FavoriteWord favorite = FavoriteWord.builder()
                .user(user)
                .myWordMnemonic(myWordMnemonic)
                .build();

        favoriteWordRepository.save(favorite);
    }

    public void removeMyWordFavorite(Long myWordMnemonicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        Optional<FavoriteWord> optionalFavorite = favoriteWordRepository
                .findByUser_UserIdAndMyWordMnemonic_MyWordMnemonicId(userId, myWordMnemonicId);

        FavoriteWord favorite = optionalFavorite
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기 항목이 존재하지 않습니다."));

        favoriteWordRepository.delete(favorite);
    }
}
