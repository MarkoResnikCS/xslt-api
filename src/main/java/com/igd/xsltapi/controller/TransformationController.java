package com.igd.xsltapi.controller;

import com.igd.xsltapi.payload.TransformationDto;
import com.igd.xsltapi.payload.UploadFileResponse;
import com.igd.xsltapi.service.FileStorageService;
import com.igd.xsltapi.service.TransformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.saxon.s9api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/transformations")
@Api(value = "Transformations")
public class TransformationController {

    private static final Logger logger = LoggerFactory.getLogger(TransformationController.class);

    private final TransformationService transformationService;

    @Autowired
    private FileStorageService fileStorageService;

    public TransformationController(TransformationService transformationService) {
        this.transformationService = transformationService;
    }

    @PostMapping("/transform")
    public UploadFileResponse transform(@RequestParam("file") MultipartFile file) throws IOException {
        Processor proc = new Processor(false);
        InputStream xsl = new ByteArrayInputStream(getXsl().getBytes(StandardCharsets.UTF_8));
//            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newFileName = "output-" + new Date().getTime() + "." + "html";//fileExtension;
        try {
            XsltTransformer trans = proc.newXsltCompiler().compile(new StreamSource(xsl)).load();
            trans.setInitialContextNode(proc.newDocumentBuilder().build(new StreamSource(file.getInputStream())));
            Serializer out = proc.newSerializer(new File(fileStorageService.getFileStorageLocation().toString(), newFileName));
//            out.setOutputProperty(Serializer.Property.METHOD, "html");
//            out.setOutputProperty(Serializer.Property.INDENT, "yes");
            trans.setDestination(out);
            trans.transform();
        } catch (SaxonApiException e) {
            e.printStackTrace();
        }

//        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/transformations/downloadFile/")
                .path(newFileName)
                .toUriString();

        return new UploadFileResponse(newFileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/transformations/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Creation")
    public ResponseEntity<TransformationDto> createTransformation(@Valid @RequestBody TransformationDto transformationDto) {
        return new ResponseEntity<>(transformationService.createTransformation(transformationDto), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "All")
    public List<TransformationDto> getAllTransformations() {
        return transformationService.getAllTransformations();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Single")
    public ResponseEntity<TransformationDto> getTransformationById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(transformationService.getTransformationById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update")
    public ResponseEntity<TransformationDto> updateTransformationBy(@Valid @RequestBody TransformationDto transformationDto, @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(transformationService.updateTransformation(transformationDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Deletion")
    public ResponseEntity<String> deleteTransformation(@PathVariable(name = "id") long id) {
        transformationService.deleteTransformationById(id);
        return new ResponseEntity<>("Transformation successfully deleted", HttpStatus.OK);
    }

    private String getXsl() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<xsl:stylesheet version=\"2.0\"\n" +
                "    xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"\n" +
                "    xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\n" +
                "    xmlns:fn=\"http://www.w3.org/2005/xpath-functions\">\n" +
                "    <xsl:output method=\"html\" />\n" +
                "    <xsl:template match=\"/\">\n" +
                "        <html>\n" +
                "            <body>\n" +
                "                <h2>Vogella Trainings</h2>\n" +
                "                <xsl:apply-templates />\n" +
                "            </body>\n" +
                "        </html>\n" +
                "    </xsl:template>\n" +
                "    <xsl:template match=\"online-trainings\">\n" +
                "        <ul>\n" +
                "            <xsl:apply-templates />\n" +
                "        </ul>\n" +
                "    </xsl:template>\n" +
                "    <xsl:template match=\"training\">\n" +
                "        <li>\n" +
                "            <xsl:value-of select=\"@name\" />\n" +
                "        </li>\n" +
                "    </xsl:template>\n" +
                "</xsl:stylesheet>";
    }
}
