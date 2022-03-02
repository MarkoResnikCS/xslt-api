package com.igd.xsltapi.service;

import com.igd.xsltapi.payload.TransformationDto;

import java.util.List;

public interface TransformationService {
    TransformationDto createTransformation(TransformationDto transformationDto);
    List<TransformationDto> getAllTransformations();
    TransformationDto getTransformationById(long id);
    TransformationDto updateTransformation(TransformationDto transformationDto, long id);
    void deleteTransformationById(long id);
}
