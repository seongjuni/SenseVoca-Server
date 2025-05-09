package com.sensevoca.backend.service;

import com.sensevoca.backend.domain.BasicWord;
import com.sensevoca.backend.dto.basicword.GetBasicWordListResponse;
import com.sensevoca.backend.dto.mywordbook.GetMyWordbookListResponse;
import com.sensevoca.backend.repository.BasicWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicWordService {

    private final BasicWordRepository basicWordRepository;

    public List<GetBasicWordListResponse> getBasicWordList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

//        List<BasicWord> basicWords = basicWordRepository.findAllByUserId(userId);
//
//        return wordbooks.stream()
//                .map(wordbook -> GetMyWordbookListResponse.builder()
//                        .id(wordbook.getId())
//                        .title(wordbook.getTitle())
//                        .wordCount(wordbook.getWordCount())
//                        .lastAccessedAt(wordbook.getLastAccessedAt())
//                        .build())
//                .collect(Collectors.toList());

        return null;
    }
}
