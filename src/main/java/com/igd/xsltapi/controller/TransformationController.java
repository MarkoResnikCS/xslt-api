package com.igd.xsltapi.controller;

import com.igd.xsltapi.payload.TransformationDto;
import com.igd.xsltapi.payload.UploadFileResponse;
import com.igd.xsltapi.service.FileStorageService;
import com.igd.xsltapi.service.TransformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XsltTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

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
    public UploadFileResponse transform(@RequestParam("file") MultipartFile file) throws IOException, SaxonApiException {
        Processor proc = new Processor(false);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!fileExtension.equalsIgnoreCase("XML")) {
            throw new IllegalArgumentException("XML files supported only");
        }
        String newFileName = "output-" + new Date().getTime() + "." + fileExtension;
        try {
            InputStream xsl = new ByteArrayInputStream(getXsl().getBytes(UTF_8));
            XsltTransformer trans = proc.newXsltCompiler().compile(new StreamSource(xsl)).load();
            trans.setInitialContextNode(proc.newDocumentBuilder().build(new StreamSource(file.getInputStream())));
            Serializer out = proc.newSerializer(new File(fileStorageService.getFileStorageLocation().toString(), newFileName));
            trans.setDestination(out);
            trans.transform();
        } catch (SaxonApiException e) {
            e.printStackTrace();
            throw e;
        }

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
        if (contentType == null) {
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

    private String getXsl() throws IOException {
        File file = ResourceUtils.getFile("classpath:stylesheet.xsl");
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader.lines().collect(Collectors.joining("\n"));
    }

}
