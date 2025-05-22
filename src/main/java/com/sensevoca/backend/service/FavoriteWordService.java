package com.sensevoca.backend.service;

import com.sensevoca.backend.domain.FavoriteWord;
import com.sensevoca.backend.domain.MyWordMnemonic;
import com.sensevoca.backend.domain.User;
import com.sensevoca.backend.repository.FavoriteWordRepository;
import com.sensevoca.backend.repository.MyWordMnemonicRepository;
import com.sensevoca.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteWordService {

    private final FavoriteWordRepository favoriteWordRepository;
    private final UserRepository userRepository;
    private final MyWordMnemonicRepository myWordMnemonicRepository;

    public void addMyWordFavorite(Long myWordMnemonicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        // 1. 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 2. 니모닉 조회
        MyWordMnemonic myWordMnemonic = myWordMnemonicRepository.findById(myWordMnemonicId)
                .orElseThrow(() -> new IllegalArgumentException("해당 나만의 단어가 존재하지 않습니다."));

        if (favoriteWordRepository.existsByUserIdAndMyWordMnemonicId(userId, myWordMnemonicId)) {
            throw new IllegalStateException("이미 즐겨찾기 되어 있음");
        }

        FavoriteWord favorite = FavoriteWord.builder()
                .user(user)
                .myWordMnemonic(myWordMnemonic)
                .build();

        favoriteWordRepository.save(favorite);
    }
}
