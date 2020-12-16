package com.newsletter.axon.web.rest;

import com.newsletter.axon.domain.Image;
import com.newsletter.axon.service.ImageService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

@CrossOrigin()
@RestController
@RequestMapping("/api")
public class ImageResource {

    private static final String COULD_NOT_UPLOAD_THE_FILE = "Could not upload the file: ";
    private static final String IMAGE = "image";
    private final ImageService imageService;

    public ImageResource(final ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Image> uploadFile(@RequestParam(IMAGE) final MultipartFile file) throws FileUploadException {
        try {
            final Image image = imageService.store(file);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(image);
        } catch (Exception e) {
            throw new FileUploadException((COULD_NOT_UPLOAD_THE_FILE + file.getOriginalFilename() + "!"));
        }
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable final String id) throws FileNotFoundException {
        final Image image = imageService.getFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .body(image.getData());
    }
}
