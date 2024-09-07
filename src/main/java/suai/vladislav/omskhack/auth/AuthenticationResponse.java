package suai.vladislav.omskhack.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import suai.vladislav.omskhack.user.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
}
