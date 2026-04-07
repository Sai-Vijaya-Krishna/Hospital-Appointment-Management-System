package com.example.hospital.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name="appointments")
public class Appointment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="patient_id") private User patient;
    @ManyToOne @JoinColumn(name="doctor_id") private Doctor doctor;
    private LocalDate appointmentDate;
    private String timeSlot;         // "10:00 AM"
    private Integer tokenNumber;
    private String status = "PENDING"; // PENDING,CONFIRMED,COMPLETED,CANCELLED
    private String symptoms;
    private String notes;
    private LocalDateTime bookedAt = LocalDateTime.now();

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public User getPatient(){return patient;} public void setPatient(User p){this.patient=p;}
    public Doctor getDoctor(){return doctor;} public void setDoctor(Doctor d){this.doctor=d;}
    public LocalDate getAppointmentDate(){return appointmentDate;} public void setAppointmentDate(LocalDate d){this.appointmentDate=d;}
    public String getTimeSlot(){return timeSlot;} public void setTimeSlot(String t){this.timeSlot=t;}
    public Integer getTokenNumber(){return tokenNumber;} public void setTokenNumber(Integer t){this.tokenNumber=t;}
    public String getStatus(){return status;} public void setStatus(String s){this.status=s;}
    public String getSymptoms(){return symptoms;} public void setSymptoms(String s){this.symptoms=s;}
    public String getNotes(){return notes;} public void setNotes(String n){this.notes=n;}
    public LocalDateTime getBookedAt(){return bookedAt;} public void setBookedAt(LocalDateTime b){this.bookedAt=b;}
}
