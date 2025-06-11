package com.sensevoca.backend.controller;

import com.sensevoca.backend.domain.Interest;
import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.interest.GetInterestResponse;
import com.sensevoca.backend.repository.InterestRepository;
import com.sensevoca.backend.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "interests", description = "유저 API")
@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestController {
    private final InterestService interestService;

    @GetMapping("/list")
    @Operation(summary = "관심사 리스트")
    public ResponseEntity<ResponseDTO<List<GetInterestResponse>>> getInterestList() {
        List<Interest> interests = interestService.getAllInterests(); // 서비스에서 가져옴

        List<GetInterestResponse> responseList = interests.stream()
                .map(interest -> new GetInterestResponse(interest.getInterestId(), interest.getType()))
                .toList();

        ResponseDTO<List<GetInterestResponse>> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("관심사 리스트 가져오기 성공");
        response.setData(responseList);

        return ResponseEntity.ok(response);
    }
}
