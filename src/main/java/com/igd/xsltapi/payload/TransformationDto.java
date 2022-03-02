package com.igd.xsltapi.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "Transformation model")
public class TransformationDto {
    @ApiModelProperty(value = "Transformation ID")
    private long id;

    @NotEmpty
    @Size(max = 50, message = "Transformation content may be 50 characters long at max")
    @ApiModelProperty(value = "Transformation Content")
    private String content;

}
