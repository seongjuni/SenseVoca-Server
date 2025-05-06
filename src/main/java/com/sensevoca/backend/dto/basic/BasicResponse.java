package com.sensevoca.backend.dto.basic;

import com.sensevoca.backend.entity.basic.Basic;
import lombok.Getter;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Getter
public class BasicResponse {
    private Long basicId;
    private String basicTitle;
    private String basicType;
    private String basicOfferedBy;
    private long daylistCount;

    public BasicResponse() { }
    public BasicResponse(Long basicId, String basicTitle, String basicType, String basicOfferedBy, Long daylistCount)
    {
        this.basicId = basicId;
        this.basicTitle = basicTitle;
        this.basicType = basicType;
        this.basicOfferedBy = basicOfferedBy;
        this.daylistCount = daylistCount;
    }
}
