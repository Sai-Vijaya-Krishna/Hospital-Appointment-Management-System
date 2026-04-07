package com.example.hospital.controller;

import com.example.hospital.entity.*;
import com.example.hospital.repository.*;
import com.example.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired private AppointmentService apptService;
    @Autowired private AppointmentRepository apptRepo;
    @Autowired private DoctorRepository doctorRepo;
    @Autowired private DepartmentRepository deptRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder encoder;

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        return ResponseEntity.ok(apptService.getDashboardStats());
    }

    // ✅ FIX: Convert raw Appointment entities to Maps to avoid JSON serialization errors
    @GetMapping("/appointments")
    public ResponseEntity<?> allAppointments() {
        List<Appointment> list = apptRepo.findAllByOrderByBookedAtDesc();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Appointment a : list) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", a.getId());
            m.put("tokenNumber", a.getTokenNumber());
            m.put("status", a.getStatus());
            m.put("appointmentDate", a.getAppointmentDate() != null ? a.getAppointmentDate().toString() : "");
            m.put("timeSlot", a.getTimeSlot());
            m.put("symptoms", a.getSymptoms());
            m.put("bookedAt", a.getBookedAt() != null ? a.getBookedAt().toString() : "");

            // Patient details
            if (a.getPatient() != null) {
                Map<String, Object> patient = new HashMap<>();
                patient.put("id",    a.getPatient().getId());
                patient.put("name",  a.getPatient().getName());
                patient.put("email", a.getPatient().getEmail());
                patient.put("phone", a.getPatient().getPhone());
                m.put("patient", patient);
            }

            // Doctor details
            if (a.getDoctor() != null) {
                Map<String, Object> doctor = new HashMap<>();
                doctor.put("id", a.getDoctor().getId());
                if (a.getDoctor().getUser() != null) {
                    doctor.put("name",  a.getDoctor().getUser().getName());
                    doctor.put("email", a.getDoctor().getUser().getEmail());
                }
                doctor.put("specialization", a.getDoctor().getSpecialization());
                if (a.getDoctor().getDepartment() != null) {
                    doctor.put("department", a.getDoctor().getDepartment().getName());
                }
                m.put("doctor", doctor);
            }
            result.add(m);
        }
        return ResponseEntity.ok(result);
    }

    // ✅ FIX: Convert Doctor entities to Maps
    @GetMapping("/doctors")
    public ResponseEntity<?> allDoctors() {
        List<Doctor> list = doctorRepo.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Doctor d : list) {
            Map<String, Object> m = new HashMap<>();
            m.put("id",              d.getId());
            m.put("specialization",  d.getSpecialization());
            m.put("qualification",   d.getQualification());
            m.put("experienceYears", d.getExperienceYears());
            m.put("consultationFee", d.getConsultationFee());
            m.put("rating",          d.getRating());
            m.put("availableDays",   d.getAvailableDays());
            m.put("availableFrom",   d.getAvailableFrom());
            m.put("availableTo",     d.getAvailableTo());
            m.put("available",       d.isAvailable());

            if (d.getUser() != null) {
                Map<String, Object> user = new HashMap<>();
                user.put("id",    d.getUser().getId());
                user.put("name",  d.getUser().getName());
                user.put("email", d.getUser().getEmail());
                user.put("phone", d.getUser().getPhone());
                m.put("user", user);
            }
            if (d.getDepartment() != null) {
                Map<String, Object> dept = new HashMap<>();
                dept.put("id",   d.getDepartment().getId());
                dept.put("name", d.getDepartment().getName());
                dept.put("icon", d.getDepartment().getIcon());
                m.put("department", dept);
            }
            result.add(m);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/doctors")
    public ResponseEntity<?> addDoctor(@RequestBody Map<String, Object> req) {
        try {
            User u = new User();
            u.setName(req.get("name").toString());
            u.setEmail(req.get("email").toString());
            u.setPassword(encoder.encode("doctor123"));
            u.setPhone(req.get("phone") != null ? req.get("phone").toString() : "");
            u.setRole("DOCTOR");
            userRepo.save(u);

            Department dept = deptRepo.findById(
                Long.parseLong(req.get("departmentId").toString())).orElseThrow();
            Doctor doc = new Doctor();
            doc.setUser(u);
            doc.setDepartment(dept);
            doc.setSpecialization(req.get("specialization").toString());
            doc.setQualification(req.get("qualification").toString());
            doc.setExperienceYears(Integer.parseInt(req.get("experienceYears").toString()));
            doc.setConsultationFee(Double.parseDouble(req.get("consultationFee").toString()));
            doc.setAvailableDays("MON,TUE,WED,THU,FRI");
            doc.setAvailableFrom("09:00");
            doc.setAvailableTo("17:00");
            doctorRepo.save(doc);
            return ResponseEntity.ok(Map.of("message", "Doctor added. Default password: doctor123"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/doctors/{id}/toggle")
    public ResponseEntity<?> toggleDoctor(@PathVariable Long id) {
        Doctor doc = doctorRepo.findById(id).orElseThrow();
        doc.setAvailable(!doc.isAvailable());
        doctorRepo.save(doc);
        return ResponseEntity.ok(Map.of("available", doc.isAvailable()));
    }

    @GetMapping("/departments")
    public ResponseEntity<?> allDepts() {
        return ResponseEntity.ok(deptRepo.findAll());
    }

    @PostMapping("/departments")
    public ResponseEntity<?> addDept(@RequestBody Department dept) {
        return ResponseEntity.ok(deptRepo.save(dept));
    }

    // ✅ FIX: Convert Patient users to safe Maps (avoids exposing any sensitive fields)
    @GetMapping("/patients")
    public ResponseEntity<?> allPatients() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (User u : userRepo.findAll()) {
            if (!"PATIENT".equals(u.getRole())) continue;
            Map<String, Object> m = new HashMap<>();
            m.put("id",         u.getId());
            m.put("name",       u.getName());
            m.put("email",      u.getEmail());
            m.put("phone",      u.getPhone());
            m.put("age",        u.getAge());
            m.put("bloodGroup", u.getBloodGroup());
            result.add(m);
        }
        return ResponseEntity.ok(result);
    }
}