package com.example.hospital.controller;
import com.example.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/api/appointments") @CrossOrigin(origins="*")
public class AppointmentController {
    @Autowired private AppointmentService apptService;

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody Map<String,Object> req, Authentication auth) {
        try { return ResponseEntity.ok(apptService.bookAppointment(auth.getName(), req)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error",e.getMessage())); }
    }

    @GetMapping("/my")
    public ResponseEntity<?> myAppointments(Authentication auth) {
        return ResponseEntity.ok(apptService.getPatientAppointments(auth.getName()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody Map<String,String> body,
                                          Authentication auth) {
        try { return ResponseEntity.ok(apptService.updateStatus(id, body.get("status"), auth.getName())); }
        catch (Exception e) { return ResponseEntity.badRequest().body(Map.of("error",e.getMessage())); }
    }
}
