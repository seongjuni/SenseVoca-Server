package com.sensevoca.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sensevoca.backend.dto.ai.CreateMnemonicExampleRequest;
import com.sensevoca.backend.dto.ai.CreateMnemonicExampleResponse;
import com.sensevoca.backend.entity.Interest;
import com.sensevoca.backend.entity.MnemonicExample;
import com.sensevoca.backend.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AiService {

    private final WebClient webClient;
    private final InterestRepository interestRepository;

    public MnemonicExample generateMnemonicExample(String word, Long interestId, String meaning) {
        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 관심사를 찾을 수 없습니다: id=" + interestId));

        String interestType = interest.getType();

        CreateMnemonicExampleRequest request = new CreateMnemonicExampleRequest(word, meaning, interestType);

        CreateMnemonicExampleResponse response = webClient.post()
                .uri("/api/ai/generate-mnemonic")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateMnemonicExampleResponse.class)
                .block(); // 동기적으로 응답 받을 때 사용

        return MnemonicExample.builder()
                .word(word)
                .meaning(meaning)
                .exampleKor(response.getExampleKor())
                .exampleEng(response.getExampleEng())
                .imageUrl(response.getImageUrl())
                .partOfSpeech(response.getPartOfSpeech())
                .phoneticUs(response.getPhoneticUs())
                .phoneticUk(response.getPhoneticUk())
                .phoneticAus(response.getPhoneticAus())
                .interest(Interest.builder().id(interestId).build()) // interest 객체가 필요하다면 이렇게
                .createdAt(LocalDateTime.now())
                .build();
    }
}
