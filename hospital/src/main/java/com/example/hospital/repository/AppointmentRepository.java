package com.example.hospital.repository;
import com.example.hospital.entity.Appointment;
import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByPatientOrderByAppointmentDateDesc(User patient);
    List<Appointment> findByDoctorAndAppointmentDateOrderByTokenNumber(Doctor doctor, LocalDate date);
    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate date);
    boolean existsByDoctorAndAppointmentDateAndTimeSlot(Doctor doctor, LocalDate date, String slot);
    List<Appointment> findAllByOrderByBookedAtDesc();
    long countByStatus(String status);
    long countByAppointmentDate(LocalDate date);
}
