package com.example.hospital.controller;
import com.example.hospital.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/auth") @CrossOrigin(origins="*")
public class AuthController {
    @Autowired private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> req) {
        try { return ResponseEntity.ok(authService.register(req)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error",e.getMessage())); }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> req) {
        try { return ResponseEntity.ok(authService.login(req)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error",e.getMessage())); }
    }
}
