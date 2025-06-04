package com.sensevoca.backend.service;

import com.sensevoca.backend.domain.MyWordMnemonic;
import com.sensevoca.backend.domain.WordInfo;
import com.sensevoca.backend.dto.ai.CreateMnemonicExampleRequest;
import com.sensevoca.backend.dto.ai.CreateMnemonicExampleResponse;
import com.sensevoca.backend.dto.ai.GetPronunciationResponse;
import com.sensevoca.backend.domain.Interest;
import com.sensevoca.backend.dto.ai.GetWordPhoneticsRequest;
import com.sensevoca.backend.dto.ai.GetWordPhoneticsResponse;
import com.sensevoca.backend.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.File;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AiService {

    @Qualifier("multipartWebClient")
    private final WebClient multipartWebClient;
    private final WebClient webClient;
    private final InterestRepository interestRepository;

    public GetWordPhoneticsResponse getWordPhonetics(String word, String meaning) {
        GetWordPhoneticsRequest request = new GetWordPhoneticsRequest(word, meaning);

        GetWordPhoneticsResponse response = webClient.post()
                .uri("/api/v1/ai/word-phonetics")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GetWordPhoneticsResponse.class)
                .block();

        return response;
    }

    public MyWordMnemonic generateMnemonicExample(WordInfo wordinfo, Long interestId, String meaning) {
        Interest interest = interestRepository.findById(interestId)
                .orElseThrow(() -> new IllegalArgumentException("해당 관심사를 찾을 수 없습니다: id=" + interestId));

        String interestType = interest.getType();

        CreateMnemonicExampleRequest request = new CreateMnemonicExampleRequest(wordinfo.getWord(), meaning, interestType);

        CreateMnemonicExampleResponse response = webClient.post()
                .uri("/api/v1/ai/generate-mnemonic")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CreateMnemonicExampleResponse.class)
                .block(); // 동기적으로 응답 받을 때 사용

        return MyWordMnemonic.builder()
                .wordInfo(wordinfo)
                .interest(interest)
                .meaning(response.getMeaning())
                .association(response.getAssociation())
                .exampleKor(response.getExampleKor())
                .exampleEng(response.getExampleEng())
                .imageUrl(response.getImageUrl())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public GetPronunciationResponse evaluatePronunciation(String word, String country, File audioFile)
    {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("word", word);
        builder.part("country", country);
        builder.part("audio", new FileSystemResource(audioFile))
                .contentType(MediaType.APPLICATION_OCTET_STREAM);

        return multipartWebClient.post()
                .uri("api/v1/ai/evaluate-pronunciation")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(GetPronunciationResponse.class)
                .block();
    }
}
