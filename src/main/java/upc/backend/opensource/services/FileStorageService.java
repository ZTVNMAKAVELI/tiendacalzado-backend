package upc.backend.opensource.services;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
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

            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            storage.createFrom(blobInfo, file.getInputStream());

            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("No se pudo almacenar el archivo en Google Cloud Storage", ex);
        }
    }
}