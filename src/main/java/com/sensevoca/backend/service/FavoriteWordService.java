package com.sensevoca.backend.service;

import com.sensevoca.backend.domain.FavoriteWord;
import com.sensevoca.backend.repository.FavoriteWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteWordService {
    private final FavoriteWordRepository favoriteWordRepository;

    public void addMyWordFavorite(Long myWordMnemonicId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

    }
}
