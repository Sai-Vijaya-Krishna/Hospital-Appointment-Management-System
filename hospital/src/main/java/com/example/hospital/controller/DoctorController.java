package com.example.hospital.controller;

import com.example.hospital.entity.*;
import com.example.hospital.repository.*;
import com.example.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "*")
public class DoctorController {

    @Autowired private AppointmentService apptService;
    @Autowired private AppointmentRepository apptRepo;
    @Autowired private PrescriptionRepository prescRepo;
    @Autowired private PrescriptionItemRepository prescItemRepo; // ✅ FIX 1: Added missing repo
    @Autowired private DoctorRepository doctorRepo;

    // ── FIX 2: Removed unused userRepo and messaging injections ──

    @GetMapping("/queue")
    public ResponseEntity<?> todayQueue(Authentication auth) {
        return ResponseEntity.ok(apptService.getDoctorTodayQueue(auth.getName()));
    }

    @PutMapping("/appointment/{id}/status")
    public ResponseEntity<?> updateAppt(@PathVariable Long id,
                                        @RequestBody Map<String, String> body,
                                        Authentication auth) {
        try {
            return ResponseEntity.ok(
                apptService.updateStatus(id, body.get("status"), auth.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/prescription")
    public ResponseEntity<?> savePrescription(@RequestBody Map<String, Object> req,
                                              Authentication auth) {
        try {
            Long apptId = Long.parseLong(req.get("appointmentId").toString());
            Appointment appt = apptRepo.findById(apptId)
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));

            Prescription p = prescRepo.findByAppointmentId(apptId)
                    .orElse(new Prescription());
            p.setAppointment(appt);
            p.setDiagnosis(req.get("diagnosis").toString());
            p.setNotes(req.get("notes") != null ? req.get("notes").toString() : "");
            p.setFollowUpDate(req.get("followUpDate") != null
                    ? req.get("followUpDate").toString() : "");
            Prescription saved = prescRepo.save(p);

            // ✅ FIX 3: Delete old medicines before saving new ones to prevent duplicates
            prescItemRepo.deleteByPrescriptionId(saved.getId());

            // ✅ FIX 4: Save medicines correctly using PrescriptionItemRepository
            @SuppressWarnings("unchecked")
            List<Map<String, String>> meds =
                    (List<Map<String, String>>) req.get("medicines");

            if (meds != null && !meds.isEmpty()) {
                List<PrescriptionItem> items = new ArrayList<>();
                for (Map<String, String> m : meds) {
                    PrescriptionItem pi = new PrescriptionItem();
                    pi.setPrescription(saved);
                    pi.setMedicineName(m.get("name"));
                    pi.setDosage(m.get("dosage"));
                    pi.setFrequency(m.get("frequency"));
                    pi.setDuration(m.get("duration"));
                    pi.setInstructions(m.get("instructions"));
                    items.add(pi);
                }
                prescItemRepo.saveAll(items); // ✅ Direct save — no cascade dependency
            }

            // Mark appointment as completed
            appt.setStatus("COMPLETED");
            apptRepo.save(appt);

            return ResponseEntity.ok(Map.of("message", "Prescription saved", "id", saved.getId()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/prescription/{apptId}")
    public ResponseEntity<?> getPrescription(@PathVariable Long apptId) {
        return prescRepo.findByAppointmentId(apptId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

	public DoctorRepository getDoctorRepo() {
		return doctorRepo;
	}

	public void setDoctorRepo(DoctorRepository doctorRepo) {
		this.doctorRepo = doctorRepo;
	}
}