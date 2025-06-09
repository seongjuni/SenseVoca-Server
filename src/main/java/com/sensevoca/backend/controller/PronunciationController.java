package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.ai.GetPronunciationResponse;
import com.sensevoca.backend.service.AiService;
import com.sensevoca.backend.service.PronunciationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@Tag(name = "evaluate-pronunciation", description = "발음 평가 API")
@RequestMapping("/api/evaluate-pronunciation")
@RequiredArgsConstructor
public class PronunciationController {
    private final PronunciationService pronunciationService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<GetPronunciationResponse>> evaluate_pronunciation(
            @RequestPart String word,
            @RequestPart String country,
            @RequestPart MultipartFile audio)
    {
        try
        {
            GetPronunciationResponse result = pronunciationService.evaluatePronunciation(word, country, audio);
            ResponseDTO<GetPronunciationResponse> response = new ResponseDTO<>();

            response.setStatus(true);
            response.setMessage("발음 평가 성공");
            response.setData(result);
            return ResponseEntity.ok(response);
        }
        catch (IOException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
