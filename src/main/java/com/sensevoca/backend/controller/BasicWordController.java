package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.basicword.GetBasicWordListResponse;
import com.sensevoca.backend.dto.mywordbook.GetMyWordbookListResponse;
import com.sensevoca.backend.service.BasicWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "basicwords", description = "기본 제공 단어 API")
@RestController
@RequestMapping("/api/basicwords")
@RequiredArgsConstructor
public class BasicWordController {
    private final BasicWordService basicWordService;

    @GetMapping("/list")
    @Operation(summary = "기본제공 단어장 리스트")
    public ResponseEntity<ResponseDTO<List<GetBasicWordListResponse>>> getBasicWordList() {
        List<GetBasicWordListResponse> wordbooks = basicWordService.getBasicWordList();

        ResponseDTO<List<GetBasicWordListResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("");
        response.setData(wordbooks);

        return ResponseEntity.ok(response);
    }
}
