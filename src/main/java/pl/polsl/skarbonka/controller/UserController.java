package pl.polsl.skarbonka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.polsl.skarbonka.error.exception.BadRequestException;
import pl.polsl.skarbonka.error.exception.NotFoundException;
import pl.polsl.skarbonka.error.exception.PermissionDeniedException;
import pl.polsl.skarbonka.error.exception.UnauthorizedException;
import pl.polsl.skarbonka.model.User;
import pl.polsl.skarbonka.request.LoginRequest;
import pl.polsl.skarbonka.request.UserCreateRequest;
import pl.polsl.skarbonka.request.UserUpdateRequest;
import pl.polsl.skarbonka.response.JwtResponse;
import pl.polsl.skarbonka.response.MessageResponse;
import pl.polsl.skarbonka.security.AuthHelper;
import pl.polsl.skarbonka.security.jwt.JwtUtil;
import pl.polsl.skarbonka.security.userDetails.UserDetailsImpl;
import pl.polsl.skarbonka.service.UserService;
import pl.polsl.skarbonka.util.ResponseEntityUtil;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final AuthHelper authHelper;
    private final ResponseEntityUtil responseEntityUtil;

    public UserController(
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager, AuthHelper authHelper, ResponseEntityUtil responseEntityUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.authHelper = authHelper;
        this.responseEntityUtil = responseEntityUtil;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable  Long id) {
        return ResponseEntity.of(userService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MessageResponse> modifyUserWithId(@PathVariable Long id, @RequestBody @Validated UserUpdateRequest userUpdateRequest, Authentication authentication) {
        User userToModify = userService.findById(id).orElseThrow(NotFoundException::new);
        if(userToModify.getEmail().equals(authentication.getName()) || authHelper.isAdmin(authentication)) {
            User userWithNewData = updateUserWithRequest(userToModify, userUpdateRequest);
            ResponseEntity<MessageResponse> successfullyResponse = responseEntityUtil.successfulMessageResponseEntity("User successfully modified");
            if (userUpdateRequest.getRole() == null || userUpdateRequest.getRole().equals(userToModify.getRole())) {
                userService.modifyUser(userWithNewData);
                return successfullyResponse;
            } else if (authHelper.isAdmin(authentication)) {
                userService.modifyUser(userWithNewData);
                return successfullyResponse;
            }
        }
        throw new PermissionDeniedException(authHelper.getRoleOfAuthentication(authentication));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()).get(0);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                role));
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<MessageResponse> registerUserWithEmailAndPassword(@RequestBody @Validated UserCreateRequest userCreateRequest) throws ResponseStatusException {
        if (emailExists(userCreateRequest.getEmail())) {
            throw new BadRequestException("User with provided e-mail already exists");
        }
        User newUser = createUserFromRequest(userCreateRequest);
        userService.saveUser(newUser);
        return responseEntityUtil.successfulMessageResponseEntity("User successfully added");
    }

    private boolean emailExists(String email) {
        return userService.findByEmail(email).isPresent();
    }

    private User createUserFromRequest(UserCreateRequest userCreateRequest) {
        User user =  new User();
        user.setFirstName(userCreateRequest.getFirstName());
        user.setLastName(userCreateRequest.getLastName());
        user.setGender(userCreateRequest.getGender());
        user.setEmail(userCreateRequest.getEmail());
        user.setBirthDate(userCreateRequest.getBirthDate());
        user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        user.setRole(User.Role.USER);
        return user;
    }

    private User updateUserWithRequest(User user, UserUpdateRequest userUpdateRequest) {
        user.setFirstName(userUpdateRequest.getFirstName());
        user.setLastName(userUpdateRequest.getLastName());
        user.setGender(userUpdateRequest.getGender());
        user.setBirthDate(userUpdateRequest.getBirthDate());
        if(userUpdateRequest.getRole() != null) {
            user.setRole(User.Role.fromName(userUpdateRequest.getRole()));
        }
        return user;
    }
}
