package com.sensevoca.backend.controller.basic;

import com.sensevoca.backend.dto.basic.*;
import com.sensevoca.backend.entity.basic.Words;
import com.sensevoca.backend.service.basic.BasicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BasicController {

    private final BasicService basicService;

    public BasicController(BasicService basicService) { this.basicService = basicService; }

    // [1] 기본 제공 단어장 목록
    @GetMapping("/api/basic/")
    public List<BasicResponse> getBasic()
    {
        return basicService.getBasic();
    }

    // [2] DAY 리스트 목록
    @GetMapping("/api/basic/{basic_id}/daylist/")
    public List<DaylistResponse> getDaylist(@PathVariable("basic_id") Long basicId)
    {
        return basicService.getDaylist(basicId);
    }

    // [3] 단어 상세 정보
    @GetMapping("/api/basic/words/{word_id}")
    public List<WordsResponse> getWords(@PathVariable("word_id") Long wordId)
    {
        return basicService.getWords(wordId);
    }

    // [4] DAY 단어 목록
    @GetMapping("/api/basic/{daylist_id}/dayword/words")
    public List<DaywordResponse> getDayword(@PathVariable("daylist_id") Long daylistId)
    {
        return basicService.getDayword(daylistId);
    }
}
