package org.example.storage.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import net.coobird.thumbnailator.Thumbnails;
import org.example.service.FileSaveFormat;
import org.example.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    @Value("${image.storage}")
    private String uploadPath;


    @Autowired
    public FileSystemStorageService(StorageProperties properties) {

        //if(properties.getLocation().trim().length() == 0){
        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            //Paths.get(file.getOriginalFilename()))
                            Paths.get(Objects.requireNonNull(file.getOriginalFilename())))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

//    @Override
//    public String saveImages(MultipartFile file, FileSaveFormat format) throws IOException {
//        String ext = format.name().toLowerCase();
//        String randomFileName = UUID.randomUUID().toString()+"."+ext;
//        int [] sizes = {32,150,300,600,1200};
//        var bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
//        for (var size : sizes) {
//            String fileSave = rootLocation.toString()+"/"+size+"_"+randomFileName;
//            Thumbnails.of(bufferedImage).size(size, size).outputFormat(ext).toFile(fileSave);
//        }
//        return randomFileName;
//    }

    @Override
    public String saveImages(MultipartFile file, FileSaveFormat format) throws IOException {
        var bufferedImage = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        return saveBufferedImage(bufferedImage, format);
    }


    @Override
    public void init() throws IOException {
        try {
        if(!Files.exists(rootLocation))
            Files.createDirectory(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String save(MultipartFile file) throws IOException {

        String randomFileName = java.util.UUID.randomUUID().toString()+"."+getFileExtension(file);

        Path destinationFile = this.rootLocation.resolve(randomFileName).normalize().toAbsolutePath();
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return randomFileName;
    }
    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        return ""; // Return an empty string if no extension is found
    }

    @Override
    public void delete(String imageName) throws IOException
    {
        File f = new File(uploadPath+ "/" +imageName);
        if(f.exists() && !f.isDirectory()) {
            Files.delete(f.toPath());
        }
    }

    @Override
    public String updateFile(String fileName,MultipartFile file) throws IOException {
        this.delete(fileName);
        //String randomFileName = this.save(file);
        //return randomFileName;
        return this.save(file);
    }

    @Override
    public void deleteImages(String imageName) throws IOException {
        int[] sizes = {32, 150, 300, 600, 1200};
        for (var size : sizes) {
            File f = new File(uploadPath + "/" + size + "_" + imageName);
            if (f.exists() && !f.isDirectory()) {
                Files.delete(f.toPath());
            }
        }
    }


    @Override
    public String updateFiles(String fileName,MultipartFile file, FileSaveFormat format) throws IOException {
        this.deleteImages(fileName);
        //String randomFileName = this.save(file);
        //return randomFileName;
        return this.saveImages(file,format);
    }


    @Override
    public String saveImages(String fileUrl, FileSaveFormat format) throws IOException {
        try (InputStream inputStream = new URL(fileUrl).openStream()) {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            return saveBufferedImage(bufferedImage, format);
        }
    }

    private String saveBufferedImage(BufferedImage bufferedImage, FileSaveFormat format) throws IOException {
        String ext = format.name().toLowerCase();
        String randomFileName = UUID.randomUUID().toString()+"."+ext;
        int [] sizes = {32,150,300,600,1200};

        for (var size : sizes) {
            String fileSave = rootLocation.toString()+"/"+size+"_"+randomFileName;
            Thumbnails.of(bufferedImage).size(size, size).outputFormat(ext).toFile(fileSave);
        }
        return randomFileName;
    }
}
