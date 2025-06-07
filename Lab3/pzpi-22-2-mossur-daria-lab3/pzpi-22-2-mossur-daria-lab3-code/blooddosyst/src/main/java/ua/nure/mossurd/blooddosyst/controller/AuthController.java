package ua.nure.mossurd.blooddosyst.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.mossurd.blooddosyst.service.AuthService;
import ua.nure.mossurd.blooddosyst.dto.LoginRequestDto;
import ua.nure.mossurd.blooddosyst.dto.UserDto;

@RestController
@RequestMapping(value = "/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        UserDto userDto = authService.login(loginRequestDto);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", userDto.token());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(userDto);
    }
}
