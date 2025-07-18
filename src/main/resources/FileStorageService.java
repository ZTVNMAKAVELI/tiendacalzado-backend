package upc.backend.opensource.services;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Storage storage;

    //nombre del bucket
    @Value("${gcs.bucket.name}")
    private String bucketName;

    public FileStorageService(Storage storage) {
        this.storage = storage;
    }

    public String storeFile(MultipartFile file) {
        try {
            // nombre de archivo unico
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            // informacion del objeto
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            // Sube el archivo al bucket de Google Cloud Storage
            storage.create(blobInfo, file.getBytes());

            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("No se pudo almacenar el archivo en Google", ex);
        }
    }
}