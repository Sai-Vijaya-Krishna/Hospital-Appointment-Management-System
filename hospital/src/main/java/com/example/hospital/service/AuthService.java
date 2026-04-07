package com.example.hospital.service;
import com.example.hospital.entity.User;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private EmailService emailService;

    public Map<String,String> register(Map<String,String> req) {
        if (userRepo.existsByEmail(req.get("email")))
            throw new RuntimeException("Email already registered");
        User user = new User();
        user.setName(req.get("name")); user.setEmail(req.get("email"));
        user.setPassword(encoder.encode(req.get("password")));
        user.setPhone(req.get("phone"));
        if (req.get("age") != null) user.setAge(Integer.parseInt(req.get("age")));
        user.setBloodGroup(req.get("bloodGroup"));
        user.setRole("PATIENT");
        userRepo.save(user);
        emailService.sendWelcome(user.getEmail(), user.getName());
        return buildResponse(user);
    }

    public Map<String,String> login(Map<String,String> req) {
        User user = userRepo.findByEmail(req.get("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(req.get("password"), user.getPassword()))
            throw new RuntimeException("Invalid password");
        return buildResponse(user);
    }

    private Map<String,String> buildResponse(User user) {
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        Map<String,String> res = new HashMap<>();
        res.put("token", token); res.put("role", user.getRole());
        res.put("name", user.getName()); res.put("email", user.getEmail());
        res.put("id", user.getId().toString());
        return res;
    }
}
