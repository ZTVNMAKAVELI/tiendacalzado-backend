package upc.backend.opensource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Override
    public void run(String... args) throws Exception {
        // Este código se ejecutará una sola vez cuando la aplicación arranque
        System.out.println("==========================================================");
        System.out.println("LA CLAVE SECRETA JWT CARGADA ES: " + jwtSecret);
        System.out.println("==========================================================");
    }
}