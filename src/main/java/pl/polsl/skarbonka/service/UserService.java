package pl.polsl.skarbonka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.polsl.skarbonka.model.User;
import pl.polsl.skarbonka.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User modifyUser(User user) {
        user.setModificationDate(new Date());
        return userRepository.save(user);
    }
}
