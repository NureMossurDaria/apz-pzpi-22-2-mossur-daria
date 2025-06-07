package ua.nure.mossurd.blooddosyst.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import ua.nure.mossurd.blooddosyst.dto.LoginRequestDto;
import ua.nure.mossurd.blooddosyst.dto.UserDto;
import ua.nure.mossurd.blooddosyst.enums.UserRole;
import ua.nure.mossurd.blooddosyst.repository.UserRepository;
import ua.nure.mossurd.blooddosyst.security.service.JwtService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password())
        );
        String jwtToken = jwtService.generateToken(authentication);
        List<UserRole> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(s -> s.substring(5))
                .map(UserRole::valueOf)
                .toList();
        ua.nure.mossurd.blooddosyst.entity.User user = userRepository.getReferenceById(loginRequestDto.username());

        return new UserDto(user.getUsername(), jwtToken, roles, user.getLanguage(), user.getPiAgreed());
    }

    public void createUser(String username, UserRole userRole) {
        jdbcUserDetailsManager.createUser(User.builder()
                .username(username)
                .password(passwordEncoder.encode("changeme"))
                .roles(userRole.name())
                .build());
    }
}
