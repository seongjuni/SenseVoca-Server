package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.basicword.*;
import com.sensevoca.backend.service.BasicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "basic", description = "기본 제공 단어장 API")
@RequestMapping("/api/basic")
@RequiredArgsConstructor
public class BasicController {

    private final BasicService basicService;

    // [1] [BASIC] 기본 제공 단어장 목록 조회
    @GetMapping("")
    @Operation(summary = "기본 제공 단어장 목록 & Daylist 개수")
    public ResponseEntity<ResponseDTO<List<GetBasicResponse>>> getBasic()
    {
        List<GetBasicResponse> result = basicService.getBasic();

        ResponseDTO<List<GetBasicResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("기본 제공 단어장 조회 성공");
        response.setData(result);
        return ResponseEntity.ok(response);
    }

    // [2] [DAYLIST] 목록 조회 + 최근 접근 시간 UPDATE
    @PatchMapping("/{basic_id}/daylist/{daylist_id}/accessed")
    @Operation(summary = "Daylist 목록 & 최근 접근 시간 업데이터 & Dayword 개수")
    public ResponseEntity<ResponseDTO<List<GetDaylistResponse>>> getDaylistWithTime(
            @PathVariable("basic_id") Long basicId,
            @PathVariable("daylist_id") Long daylistId,
            @RequestBody UpdateDatetimeRequest request)
    {
        basicService.updateDatetime(daylistId, request.getLatestAccessedAt());

        List<GetDaylistResponse> result = basicService.getDaylist(basicId);

        ResponseDTO<List<GetDaylistResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("기본 제공 단어장의 Daylist 목록 조회 성공");
        response.setData(result);
        return ResponseEntity.ok(response);
    }


    // [3] [DAYWORD] 목록 조회
    @GetMapping("{daylist_id}/dayword")
    @Operation(summary = "Dayword 목록")
    public ResponseEntity<ResponseDTO<List<GetDaywordResponse>>> getDayword(@PathVariable("daylist_id") Long daylistId)
    {
        List<GetDaywordResponse> result = basicService.getDayword(daylistId);

        ResponseDTO<List<GetDaywordResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("Daylist에 해당하는 Dayword 목록 조회 성공");
        response.setData(result);
        return ResponseEntity.ok(response);
    }

    // [4] [BASIC WORD] 단어 상세 정보 조회
    @GetMapping("{daylist_id}/basic_word/{country}")
    @Operation(summary = "기본 제공 단어 상세 정보")
    public ResponseEntity<ResponseDTO<List<GetBasicWordResponse>>> getBasicWord(
            @PathVariable("daylist_id") Long daylistId, @PathVariable("country") String country)
    {
        List<GetBasicWordResponse> result = basicService.getBasicWord(daylistId, country);

        ResponseDTO<List<GetBasicWordResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("기본 제공 단어 상세 정보");
        response.setData(result);
        return ResponseEntity.ok(response);
    }

}
