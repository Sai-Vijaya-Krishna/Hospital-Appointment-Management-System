package com.example.hospital.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired private JavaMailSender mailSender;

    private void send(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to); msg.setSubject(subject); msg.setText(body);
            mailSender.send(msg);
        } catch (Exception e) { System.out.println("Email failed: " + e.getMessage()); }
    }

    public void sendWelcome(String to, String name) {
        send(to, "Welcome to City Hospital!", "Hi " + name + ",\n\nWelcome to City Hospital! Your account has been created.\n\nStay healthy!\nCity Hospital Team");
    }

    public void sendAppointmentConfirmation(String to, String name, String doctor, String date, String time, int token) {
        send(to, "Appointment Confirmed - City Hospital",
            "Hi " + name + ",\n\nYour appointment has been confirmed!\n\n" +
            "Doctor: " + doctor + "\nDate: " + date + "\nTime: " + time +
            "\nYour Token: #" + token +
            "\n\nPlease arrive 10 minutes early.\n\nCity Hospital");
    }

    public void sendAppointmentCancellation(String to, String name, String doctor, String date) {
        send(to, "Appointment Cancelled - City Hospital",
            "Hi " + name + ",\n\nYour appointment with " + doctor + " on " + date +
            " has been cancelled.\n\nBook again at your convenience.\n\nCity Hospital");
    }
}
