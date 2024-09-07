package suai.vladislav.omskhack.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import suai.vladislav.omskhack.user.User;
import suai.vladislav.omskhack.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .build();

        userRepository.save(user);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .build();
    }
}
