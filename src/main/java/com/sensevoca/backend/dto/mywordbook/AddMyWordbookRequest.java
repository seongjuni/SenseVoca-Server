package com.sensevoca.backend.dto.mywordbook;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddMyWordbookRequest {
    private String title;
    private List<MyWordRequest> words;
}
