package com.newsletter.axon.service;

import com.newsletter.axon.domain.Image;
import com.newsletter.axon.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(final ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image store(final MultipartFile file) throws IOException {
        final String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        final Image image = new Image();
        image.setName(fileName);
        image.setType(file.getContentType());
        image.setData(file.getBytes());

        return imageRepository.save(image);
    }

    public Image getFile(final String id) throws FileNotFoundException {
        return imageRepository.findById(Long.parseLong(id))
                .orElseThrow(FileNotFoundException::new);
    }

    public Stream<Image> getAllFiles() {
        return imageRepository.findAll().stream();
    }
}
