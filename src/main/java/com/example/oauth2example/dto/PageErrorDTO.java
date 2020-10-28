package com.example.oauth2example.dto;

import com.example.oauth2example.exception.AuthPageException;
import lombok.Data;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageErrorDTO {
    private String code;
    private String message;

    public static PageErrorDTO create(AuthPageException e) {
        PageErrorDTO dto = new PageErrorDTO();
        dto.setCode(e.getCode());
        dto.setMessage(e.getMessage());

        return dto;
    }

    public static List<PageErrorDTO>  create(List<ObjectError> objectErrors) {
        List<PageErrorDTO> pageErrors = new ArrayList<>();
        PageErrorDTO dto;

        for(ObjectError e : objectErrors) {
            dto = new PageErrorDTO();
            dto.setCode(e.getCode());
            dto.setMessage(e.getObjectName() + ": " + e.getDefaultMessage());
            pageErrors.add(dto);
        }

        return pageErrors;
    }

    public static PageErrorDTO create(Exception e) {
        PageErrorDTO dto = new PageErrorDTO();
        dto.setCode("internal_server");
        dto.setMessage(e.getMessage());

        return dto;
    }
}
