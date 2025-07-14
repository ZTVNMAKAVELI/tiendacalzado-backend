package upc.backend.opensource.payload.request;

import lombok.Data;
import java.util.Set;
@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private Set<String> role;
}