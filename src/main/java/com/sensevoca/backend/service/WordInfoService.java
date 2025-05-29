package com.sensevoca.backend.service;

import com.sensevoca.backend.domain.BasicWord;
import com.sensevoca.backend.domain.WordInfo;
import com.sensevoca.backend.dto.ai.GetWordPhoneticsResponse;
import com.sensevoca.backend.dto.wordinfo.GetWordInfosResponse;
import com.sensevoca.backend.repository.BasicWordRepository;
import com.sensevoca.backend.repository.WordInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordInfoService {

    private final WordInfoRepository wordInfoRepository;
    private final BasicWordRepository basicWordRepository;
    private final AiService aiService;

    public List<GetWordInfosResponse> getAllWordInfos() {
        return basicWordRepository.findAll().stream()
                .map(basicWord -> {
                    WordInfo wordInfo = basicWord.getWordInfo();
                    return GetWordInfosResponse.builder()
                            .wordId(wordInfo.getWordId())
                            .word(wordInfo.getWord())
                            .meaning(basicWord.getMeaning())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public WordInfo findOrGenerateWordInfo(String word, String meaning) {
        // 1. AI 서비스에 요청해서 발음 정보 받아오기
        GetWordPhoneticsResponse generatedInfo = aiService.getWordPhonetics(word, meaning);

        // 2. 받은 word, phoneticUs, phoneticUk, phoneticAus 기준으로 WordInfo 조회
        Optional<WordInfo> existingWordInfo = wordInfoRepository.findByWordAndPhoneticUsAndPhoneticUkAndPhoneticAus(
                generatedInfo.getWord(),
                generatedInfo.getPhoneticUs(),
                generatedInfo.getPhoneticUk(),
                generatedInfo.getPhoneticAus()
        );

        if (existingWordInfo.isPresent()) {
            // 3-1. 이미 존재하면 그 WordInfo 리턴
            return existingWordInfo.get();
        } else {
            // 3-2. 없으면 새 WordInfo 저장 후 리턴
            WordInfo newWordInfo = WordInfo.builder()
                    .word(generatedInfo.getWord())
                    .phoneticUs(generatedInfo.getPhoneticUs())
                    .phoneticUk(generatedInfo.getPhoneticUk())
                    .phoneticAus(generatedInfo.getPhoneticAus())
                    .build();

            return wordInfoRepository.save(newWordInfo);
        }
    }
}
