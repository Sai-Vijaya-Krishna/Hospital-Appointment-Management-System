package com.example.hospital.config;
import com.example.hospital.entity.*;
import com.example.hospital.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired private UserRepository userRepo;
    @Autowired private DepartmentRepository deptRepo;
    @Autowired private DoctorRepository doctorRepo;
    @Autowired private PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        // Admin
        if (!userRepo.existsByEmail("admin@hospital.com")) {
            User admin = new User();
            admin.setName("Admin"); admin.setEmail("admin@hospital.com");
            admin.setPassword(encoder.encode("admin123")); admin.setRole("ADMIN");
            userRepo.save(admin);
            System.out.println("Admin: admin@hospital.com / admin123");
        }

        // Departments
        String[][] depts = {
            {"Cardiology","Heart & cardiovascular diseases","❤️"},
            {"Orthopedics","Bones, joints & muscles","🦴"},
            {"Neurology","Brain & nervous system","🧠"},
            {"Dermatology","Skin, hair & nails","🩺"},
            {"Pediatrics","Children's health","👶"},
            {"ENT","Ear, Nose & Throat","👂"},
            {"Gynecology","Women's health","🌸"},
            {"General Medicine","General health & checkups","💊"}
        };
        for (String[] d : depts) {
            if (!deptRepo.existsByName(d[0])) {
                Department dept = new Department();
                dept.setName(d[0]); dept.setDescription(d[1]); dept.setIcon(d[2]);
                deptRepo.save(dept);
            }
        }

        // Doctors
        if (doctorRepo.count() == 0) {
            Object[][] docs = {
                {"Dr. Ravi Kumar","ravi@hospital.com","Cardiologist","MBBS, MD Cardiology",12,500.0,"Cardiology"},
                {"Dr. Priya Sharma","priya@hospital.com","Orthopedic Surgeon","MBBS, MS Ortho",8,400.0,"Orthopedics"},
                {"Dr. Anil Reddy","anil@hospital.com","Neurologist","MBBS, DM Neuro",15,600.0,"Neurology"},
                {"Dr. Sunita Patel","sunita@hospital.com","Dermatologist","MBBS, MD Derma",6,350.0,"Dermatology"},
                {"Dr. Kiran Babu","kiran@hospital.com","Pediatrician","MBBS, DCH",10,300.0,"Pediatrics"},
                {"Dr. Meera Nair","meera@hospital.com","ENT Specialist","MBBS, MS ENT",7,350.0,"ENT"}
            };
            for (Object[] d : docs) {
                User u = new User();
                u.setName((String)d[0]); u.setEmail((String)d[1]);
                u.setPassword(encoder.encode("doctor123")); u.setRole("DOCTOR");
                userRepo.save(u);

                Department dept = deptRepo.findAll().stream()
                    .filter(x->x.getName().equals(d[6])).findFirst().orElse(null);

                Doctor doc = new Doctor();
                doc.setUser(u); doc.setDepartment(dept);
                doc.setSpecialization((String)d[2]); doc.setQualification((String)d[3]);
                doc.setExperienceYears((Integer)d[4]); doc.setConsultationFee((Double)d[5]);
                doc.setAvailableDays("MON,TUE,WED,THU,FRI");
                doc.setAvailableFrom("09:00"); doc.setAvailableTo("17:00");
                doc.setSlotDurationMins(30);
                doctorRepo.save(doc);
            }
            System.out.println("6 doctors seeded! Password: doctor123");
        }
    }
}
