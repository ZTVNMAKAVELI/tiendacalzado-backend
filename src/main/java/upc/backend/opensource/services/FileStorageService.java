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
            // nombre de archivo unico
            String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

            // informacion del objeto
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            // Sube el archivo al bucket de Google Cloud Storage
            storage.create(blobInfo, file.getBytes());
            storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("No se pudo almacenar el archivo en Google", ex);
        }
    }
}