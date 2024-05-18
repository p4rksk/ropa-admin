package com.example.finalproject.codi;

import com.example.finalproject.domain.codi.CodiRequest;
import com.example.finalproject.domain.photo.Photo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CodiControllerTest {
    @Autowired
    private MockMvc mvc;
    private ObjectMapper om = new ObjectMapper();


    @Test
    public void codiRegister_success_test() throws JsonProcessingException {
        // given
        List<CodiRequest.SaveDTO.AppSaveDTO> codiPhotos = List.of(
                new CodiRequest.SaveDTO.AppSaveDTO("photo1", "base64string1", true, Photo.Sort.CODI),
                new CodiRequest.SaveDTO.AppSaveDTO("photo2", "base64string2", false, Photo.Sort.CODI)
        );

        List<CodiRequest.SaveDTO.ItemCodiDTO> items = List.of(
                new CodiRequest.SaveDTO.ItemCodiDTO(1, 1),
                new CodiRequest.SaveDTO.ItemCodiDTO(2, 2)
        );

        CodiRequest.SaveDTO reqDTO = new CodiRequest.SaveDTO(1, "Test Description", codiPhotos, items);
        String reqBody = om.writeValueAsString(reqDTO);
        System.out.println("reqBody = " + reqBody);
        // when

        // eye

        // then


    }
}