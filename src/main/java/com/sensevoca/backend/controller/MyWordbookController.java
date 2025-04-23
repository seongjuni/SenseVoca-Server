package com.sensevoca.backend.controller;

import com.sensevoca.backend.dto.ResponseDTO;
import com.sensevoca.backend.dto.mywordbook.AddMyWordbookRequest;
import com.sensevoca.backend.service.MyWordbookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "mywordbooks", description = "나만의 단어 API")
@RestController
@RequestMapping("/api/mywordbooks")
@RequiredArgsConstructor
public class MyWordbookController {
    private final MyWordbookService myWordbookService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Long>> addMyWordbook(
            @RequestBody AddMyWordbookRequest myWordbookRequest) {

        Long wordbookId = myWordbookService.addMyWordbook(myWordbookRequest);

        ResponseDTO<Long> response = new ResponseDTO<>();
        response.setStatus(true);
        response.setMessage("");
        response.setData(wordbookId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
