package com.sensevoca.backend.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMnemonicExampleRequest {
    private String word;
    private String meaning;
    private String interest;
}
