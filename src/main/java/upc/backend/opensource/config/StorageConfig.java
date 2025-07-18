package upc.backend.opensource.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Bean
    public Storage storage() {
        //Inicializa el cliente de Google Cloud Storage con credenciales por defecto
        return StorageOptions.getDefaultInstance().getService();
    }
}