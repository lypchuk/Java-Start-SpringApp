package org.example.storage.impl;

import org.example.service.FileSaveFormat;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init() throws IOException;

    String save(MultipartFile file) throws IOException;
    String updateFile(String fileName,MultipartFile file) throws IOException;
    void delete(String imageName) throws IOException;

    void store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
    String saveImage(MultipartFile file, FileSaveFormat format) throws IOException;

}
