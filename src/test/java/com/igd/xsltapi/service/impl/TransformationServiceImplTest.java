package com.igd.xsltapi.service.impl;

import com.igd.xsltapi.entity.Transformation;
import com.igd.xsltapi.payload.TransformationDto;
import com.igd.xsltapi.repository.TransformationRepository;
import com.igd.xsltapi.service.TransformationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service - Transformation")
class TransformationServiceImplTest {

    @Mock
    private TransformationRepository transformationRepositoryMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Captor
    private ArgumentCaptor<Transformation> transformationArgumentCaptor;

    TransformationService service;

    @BeforeEach
    public void setup() {
        service = new TransformationServiceImpl(transformationRepositoryMock, modelMapperMock);
    }

    @Test
    @DisplayName("Create")
    void givenTransformationToCreate_WhenCalled_ThenTransformationCreated() {
        long id = 1L;
        String content = "content";

        TransformationDto transformationDto = new TransformationDto();
        transformationDto.setId(id);
        transformationDto.setContent(content);

        Transformation transformation = new Transformation();
        transformation.setId(id);
        transformation.setContent(content);

        Mockito.when(modelMapperMock.map(Mockito.any(TransformationDto.class), Mockito.eq(Transformation.class))).thenReturn(transformation);
        Mockito.when(transformationRepositoryMock.save(Mockito.any(Transformation.class))).thenReturn((transformation));
        Mockito.when(modelMapperMock.map(Mockito.any(Transformation.class), Mockito.eq(TransformationDto.class))).thenReturn(transformationDto);

        // ACT
        TransformationDto actualTransformationDto = service.createTransformation(transformationDto);

        // ASSERT
        assertThat(actualTransformationDto.getId()).isEqualTo(transformationDto.getId());
        assertThat(actualTransformationDto.getContent()).isEqualTo(transformationDto.getContent());
    }

    @Test
    @DisplayName("Read all")
    void givenStoredTransformations_WhenCalled_ThenTransformationsReturned() {
        // ARRANGE
        Transformation transformation = new Transformation();
        transformation.setId(1L);
        transformation.setContent("content");
        List<Transformation> transformations = new ArrayList<>();
        transformations.add(transformation);
        Mockito.when(transformationRepositoryMock.findAll()).thenReturn(transformations);

        TransformationDto expectedTransformationDto = new TransformationDto();
        expectedTransformationDto.setId(1L);
        expectedTransformationDto.setContent("content");
        Mockito.when(modelMapperMock.map(Mockito.any(Transformation.class), Mockito.eq(TransformationDto.class))).thenReturn(expectedTransformationDto);

        // ACT
        List<TransformationDto> actualTransformationDtos = service.getAllTransformations();

        // ASSERT
        assertThat(actualTransformationDtos.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Read single")
    void givenStoredTransformation_WhenCalled_ThenTransformationReturned() {
        // ARRANGE
        long id = 1L;
        String content = "content";

        Transformation transformation = new Transformation();
        transformation.setId(id);
        transformation.setContent(content);
        Mockito.when(transformationRepositoryMock.findById(id)).thenReturn(Optional.of(transformation));

        TransformationDto expectedTransformationDto = new TransformationDto();
        expectedTransformationDto.setId(id);
        expectedTransformationDto.setContent(content);
        Mockito.when(modelMapperMock.map(Mockito.any(Transformation.class), Mockito.eq(TransformationDto.class))).thenReturn(expectedTransformationDto);

        // ACT
        TransformationDto actualTransformationDto = service.getTransformationById(id);

        // ASSERT
        assertThat(actualTransformationDto.getId()).isEqualTo(expectedTransformationDto.getId());
        assertThat(actualTransformationDto.getContent()).isEqualTo(expectedTransformationDto.getContent());
    }

    @Test
    @DisplayName("Read single not found")
    void givenTransformation_WhenTransformationNotFound_ThenExceptionThrown() {
        // ARRANGE
        long id = 1L;

        // ASSERT
        assertThatThrownBy(() -> service.getTransformationById(id)).isInstanceOf(RuntimeException.class)
                .hasMessage(String.format("No transformation found for id '%s'", id));
    }

    @Test
    @DisplayName("Update")
    void givenTransformationToUpdate_WhenCalled_ThenTransformationUpdated() {
        long id = 1L;
        String oldContend = "old";
        String newContent = "new";

        Transformation transformation = new Transformation();
        transformation.setId(id);
        transformation.setContent(oldContend);
        Mockito.when(transformationRepositoryMock.findById(id)).thenReturn(Optional.of(transformation));

        Mockito.when(transformationRepositoryMock.save(transformation)).thenReturn((transformation));

        TransformationDto transformationDto = new TransformationDto();
        transformationDto.setContent(newContent);
        Mockito.when(modelMapperMock.map(Mockito.any(Transformation.class), Mockito.eq(TransformationDto.class))).thenReturn(transformationDto);

        // ACT
        TransformationDto actualTransformationDto = service.updateTransformation(transformationDto, id);

        // ASSERT
        assertThat(actualTransformationDto.getId()).isEqualTo(transformationDto.getId());
        assertThat(actualTransformationDto.getContent()).isEqualTo(transformationDto.getContent());
    }

    @Test
    @DisplayName("Delete")
    void givenTransformationToDelete_WhenTransformationFound_ThenTransformationDeleted() {
        // ARRANGE
        Transformation transformation = new Transformation();
        transformation.setId(1L);
        Mockito.when(transformationRepositoryMock.findById(Mockito.anyLong())).thenReturn(Optional.of(transformation));

        // ACT
        service.deleteTransformationById(transformation.getId());

        // ASSERT
        Mockito.verify(transformationRepositoryMock, Mockito.times(1)).delete(transformationArgumentCaptor.capture());
        assertThat(transformationArgumentCaptor.getValue().getId()).isEqualTo(transformation.getId());
    }
}