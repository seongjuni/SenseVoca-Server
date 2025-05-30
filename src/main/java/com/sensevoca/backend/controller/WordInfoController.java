package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.wordinfo.GetWordInfosResponse;
import com.sensevoca.backend.service.WordInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "wordinfos", description = "단어 정보 API")
@RestController
@RequestMapping("/api/wordinfos")
@RequiredArgsConstructor
public class WordInfoController {

    private final WordInfoService wordInfoService;

    @GetMapping("/list")
    @Operation(summary = "단어 리스트 조회")
    public ResponseEntity<ResponseDTO<List<GetWordInfosResponse>>> getAllWordInfos() {
        List<GetWordInfosResponse> wordInfos = wordInfoService.getAllWordInfos();

        ResponseDTO<List<GetWordInfosResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("");
        response.setData(wordInfos);

        return ResponseEntity.ok(response);
    }
}
