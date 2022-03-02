package com.igd.xsltapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidFileExtensionException extends RuntimeException {
    private final String extension;

    public InvalidFileExtensionException(String extension) {
        super(String.format("Invalid file extension '%s'", extension));
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
