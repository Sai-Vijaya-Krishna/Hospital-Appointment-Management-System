package com.example.hospital.controller;
import com.example.hospital.entity.Department;
import com.example.hospital.entity.Doctor;
import com.example.hospital.repository.DepartmentRepository;
import com.example.hospital.repository.DoctorRepository;
import com.example.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/public") @CrossOrigin(origins="*")
public class PublicController {
    @Autowired private DepartmentRepository deptRepo;
    @Autowired private DoctorRepository doctorRepo;
    @Autowired private AppointmentService apptService;

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        return ResponseEntity.ok(deptRepo.findAll());
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorRepo.findByAvailableTrue());
    }

    @GetMapping("/doctors/department/{id}")
    public ResponseEntity<List<Doctor>> getDoctorsByDept(@PathVariable Long id) {
        return ResponseEntity.ok(doctorRepo.findByDepartmentIdAndAvailableTrue(id));
    }

    @GetMapping("/slots/{doctorId}")
    public ResponseEntity<?> getSlots(@PathVariable Long doctorId, @RequestParam String date) {
        try { return ResponseEntity.ok(apptService.getAvailableSlots(doctorId, date)); }
        catch (Exception e) { return ResponseEntity.badRequest().body(java.util.Map.of("error",e.getMessage())); }
    }
}
