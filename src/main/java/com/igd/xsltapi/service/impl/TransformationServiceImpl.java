package com.igd.xsltapi.service.impl;

import com.igd.xsltapi.entity.Transformation;
import com.igd.xsltapi.payload.TransformationDto;
import com.igd.xsltapi.repository.TransformationRepository;
import com.igd.xsltapi.service.TransformationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransformationServiceImpl implements TransformationService {

    private final TransformationRepository transformationRepository;
    private final ModelMapper modelMapper;

    public TransformationServiceImpl(TransformationRepository transformationRepository, ModelMapper modelMapper) {
        this.transformationRepository = transformationRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TransformationDto createTransformation(TransformationDto transformationDto) {
        return mapToDto(transformationRepository.save(mapToEntity(transformationDto)));
    }

    @Override
    public List<TransformationDto> getAllTransformations() {
        return transformationRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public TransformationDto getTransformationById(long id) {
        Transformation transformation = transformationRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("No transformation found for id '%s'", id)));
        return mapToDto(transformation);
    }

    @Override
    public TransformationDto updateTransformation(TransformationDto transformationDto, long id) {
        Transformation transformation = transformationRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("No transformation found for id '%s'", id)));
        transformation.setContent(transformationDto.getContent());
        return mapToDto(transformationRepository.save(transformation));
    }

    @Override
    public void deleteTransformationById(long id) {
        Transformation transformation = transformationRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("No transformation found for id '%s'", id)));
        transformationRepository.delete(transformation);
    }

    private TransformationDto mapToDto(Transformation transformation) {
        return modelMapper.map(transformation, TransformationDto.class);
    }

    private Transformation mapToEntity(TransformationDto transformationDto) {
        return modelMapper.map(transformationDto, Transformation.class);
    }
}
