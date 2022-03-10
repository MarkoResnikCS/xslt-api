package com.igd.xsltapi.controller;

import com.igd.xsltapi.payload.TransformationDto;
import com.igd.xsltapi.security.CustomUserDetailsService;
import com.igd.xsltapi.security.JwtAuthenticationEntryPoint;
import com.igd.xsltapi.security.JwtTokenProvider;
import com.igd.xsltapi.service.FileStorageService;
import com.igd.xsltapi.service.TransformationService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransformationController.class)
public class TransformationControllerTest {

    @MockBean
    private TransformationService transformationService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private FileStorageService fileStorageService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldListAllTransformations() throws Exception {
        TransformationDto transformationDto1 = new TransformationDto();
        transformationDto1.setId(1);
        transformationDto1.setContent("first transformation");
        TransformationDto transformationDto2 = new TransformationDto();
        transformationDto1.setId(1);
        transformationDto2.setContent("second transformation");

        Mockito.when(transformationService.getAllTransformations()).thenReturn(asList(transformationDto1, transformationDto2));

        mockMvc.perform(get("/api/transformations/")).andExpect(status().is(200));
    }

}
