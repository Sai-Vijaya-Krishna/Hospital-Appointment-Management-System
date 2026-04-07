package com.example.hospital.service;
import com.example.hospital.entity.*;
import com.example.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AppointmentService {
    @Autowired private AppointmentRepository apptRepo;
    @Autowired private DoctorRepository doctorRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private EmailService emailService;
    @Autowired private SimpMessagingTemplate messaging;

    public List<String> getAvailableSlots(Long doctorId, String date) {
        Doctor doctor = doctorRepo.findById(doctorId).orElseThrow();
        LocalDate apptDate = LocalDate.parse(date);
        List<Appointment> booked = apptRepo.findByDoctorAndAppointmentDate(doctor, apptDate);
        Set<String> bookedSlots = new HashSet<>();
        for (Appointment a : booked) bookedSlots.add(a.getTimeSlot());

        List<String> slots = new ArrayList<>();
        LocalTime from = LocalTime.parse(doctor.getAvailableFrom());
        LocalTime to   = LocalTime.parse(doctor.getAvailableTo());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm a");
        while (from.isBefore(to)) {
            String slot = from.format(fmt);
            if (!bookedSlots.contains(slot)) slots.add(slot);
            from = from.plusMinutes(doctor.getSlotDurationMins());
        }
        return slots;
    }

    public Map<String,Object> bookAppointment(String email, Map<String,Object> req) {
        User patient = userRepo.findByEmail(email).orElseThrow();
        Doctor doctor = doctorRepo.findById(Long.parseLong(req.get("doctorId").toString())).orElseThrow();
        LocalDate date = LocalDate.parse(req.get("date").toString());
        String slot = req.get("timeSlot").toString();

        if (apptRepo.existsByDoctorAndAppointmentDateAndTimeSlot(doctor, date, slot))
            throw new RuntimeException("Slot already booked. Please choose another.");

        int token = apptRepo.findByDoctorAndAppointmentDate(doctor, date).size() + 1;

        Appointment appt = new Appointment();
        appt.setPatient(patient); appt.setDoctor(doctor);
        appt.setAppointmentDate(date); appt.setTimeSlot(slot);
        appt.setTokenNumber(token); appt.setStatus("CONFIRMED");
        appt.setSymptoms(req.get("symptoms") != null ? req.get("symptoms").toString() : "");
        apptRepo.save(appt);

        emailService.sendAppointmentConfirmation(patient.getEmail(), patient.getName(),
            doctor.getUser().getName(), date.toString(), slot, token);

        // Notify doctor's queue via WebSocket
        messaging.convertAndSend("/topic/queue/" + doctorId(doctor), buildQueueMsg(appt));

        Map<String,Object> res = new HashMap<>();
        res.put("id", appt.getId()); res.put("token", token);
        res.put("status","CONFIRMED"); res.put("date", date.toString());
        res.put("slot", slot); res.put("doctor", doctor.getUser().getName());
        return res;
    }

    public List<Map<String,Object>> getPatientAppointments(String email) {
        User patient = userRepo.findByEmail(email).orElseThrow();
        List<Appointment> list = apptRepo.findByPatientOrderByAppointmentDateDesc(patient);
        List<Map<String,Object>> result = new ArrayList<>();
        for (Appointment a : list) result.add(convertAppt(a));
        return result;
    }

    public List<Map<String,Object>> getDoctorTodayQueue(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        Doctor doctor = doctorRepo.findByUser(user).orElseThrow();
        List<Appointment> list = apptRepo.findByDoctorAndAppointmentDateOrderByTokenNumber(doctor, LocalDate.now());
        List<Map<String,Object>> result = new ArrayList<>();
        for (Appointment a : list) result.add(convertAppt(a));
        return result;
    }

    public Map<String,Object> updateStatus(Long id, String status, String email) {
        Appointment appt = apptRepo.findById(id).orElseThrow();
        appt.setStatus(status);
        apptRepo.save(appt);

        if ("CANCELLED".equals(status))
            emailService.sendAppointmentCancellation(
                appt.getPatient().getEmail(), appt.getPatient().getName(),
                appt.getDoctor().getUser().getName(), appt.getAppointmentDate().toString());

        // Push token update to waiting room display
        Map<String,Object> msg = new HashMap<>();
        msg.put("appointmentId", id); msg.put("status", status);
        msg.put("token", appt.getTokenNumber());
        msg.put("doctorId", appt.getDoctor().getId());
        messaging.convertAndSend("/topic/queue/" + appt.getDoctor().getId(), msg);

        return convertAppt(appt);
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAppointments", apptRepo.count());
        stats.put("todayAppointments", apptRepo.countByAppointmentDate(LocalDate.now()));
        stats.put("confirmedToday",    apptRepo.countByStatus("CONFIRMED"));
        stats.put("completed",         apptRepo.countByStatus("COMPLETED"));
        stats.put("totalDoctors",      doctorRepo.count());
        stats.put("totalPatients",
            userRepo.findAll().stream().filter(u -> "PATIENT".equals(u.getRole())).count());

        List<Map<String, Object>> recent = new ArrayList<>();
        for (Appointment a : apptRepo.findAllByOrderByBookedAtDesc().stream().limit(5).toList()) {
            Map<String, Object> m = new HashMap<>();
            m.put("id",     a.getId());
            m.put("status", a.getStatus());
            m.put("date",   a.getAppointmentDate() != null ? a.getAppointmentDate().toString() : "");
            m.put("slot",   a.getTimeSlot());
            m.put("patientName", a.getPatient() != null ? a.getPatient().getName() : "");
            m.put("doctorName",  a.getDoctor() != null && a.getDoctor().getUser() != null
                                 ? a.getDoctor().getUser().getName() : "");
            m.put("department",  a.getDoctor() != null && a.getDoctor().getDepartment() != null
                                 ? a.getDoctor().getDepartment().getName() : "");
            recent.add(m);
        }
        stats.put("recentAppointments", recent);
        return stats;
    }

    private Map<String,Object> convertAppt(Appointment a) {
        Map<String,Object> m = new HashMap<>();
        m.put("id", a.getId());
        m.put("token", a.getTokenNumber());
        m.put("status", a.getStatus());
        m.put("date", a.getAppointmentDate() != null ? a.getAppointmentDate().toString() : "");
        m.put("slot", a.getTimeSlot());
        m.put("symptoms", a.getSymptoms());
        m.put("notes", a.getNotes());
        m.put("patientName", a.getPatient() != null ? a.getPatient().getName() : "");
        m.put("patientEmail", a.getPatient() != null ? a.getPatient().getEmail() : "");
        m.put("patientPhone", a.getPatient() != null ? a.getPatient().getPhone() : "");
        m.put("doctorName", a.getDoctor() != null ? a.getDoctor().getUser().getName() : "");
        m.put("doctorId", a.getDoctor() != null ? a.getDoctor().getId() : null);
        m.put("specialization", a.getDoctor() != null ? a.getDoctor().getSpecialization() : "");
        m.put("department", a.getDoctor() != null && a.getDoctor().getDepartment() != null ? a.getDoctor().getDepartment().getName() : "");
        return m;
    }

    private Map<String,Object> buildQueueMsg(Appointment a) {
        Map<String,Object> m = new HashMap<>();
        m.put("appointmentId", a.getId()); m.put("token", a.getTokenNumber());
        m.put("patientName", a.getPatient().getName()); m.put("status", a.getStatus());
        m.put("slot", a.getTimeSlot()); m.put("doctorId", a.getDoctor().getId());
        return m;
    }

    private Long doctorId(Doctor d) { return d.getId(); }
}
