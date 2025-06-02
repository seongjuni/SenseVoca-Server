package com.sensevoca.backend.service;

import com.sensevoca.backend.dto.ai.GetPronunciationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PronunciationService {
    private final AiService aiService;

    public GetPronunciationResponse evaluatePronunciation(String word, String country, MultipartFile audio)
            throws IOException {
        // 1) MultipartFile -> File로 변환 (임시 파일로 저장)
        File tempFile = File.createTempFile("audio-", ".wav");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(audio.getBytes());
        }

        try {
            return aiService.evaluatePronunciation(word, country, tempFile);
        } finally {
            if (tempFile != null && tempFile.exists())
                tempFile.delete();
            }
    }
}
