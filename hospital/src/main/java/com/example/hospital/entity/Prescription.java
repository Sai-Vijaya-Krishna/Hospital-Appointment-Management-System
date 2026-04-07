package com.example.hospital.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity @Table(name="prescriptions")
public class Prescription {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne @JoinColumn(name="appointment_id") private Appointment appointment;
    private String diagnosis;
    private String notes;
    private LocalDate prescribedDate = LocalDate.now();
    private String followUpDate;

    @OneToMany(mappedBy="prescription", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JsonManagedReference
    private List<PrescriptionItem> medicines;

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Appointment getAppointment(){return appointment;} public void setAppointment(Appointment a){this.appointment=a;}
    public String getDiagnosis(){return diagnosis;} public void setDiagnosis(String d){this.diagnosis=d;}
    public String getNotes(){return notes;} public void setNotes(String n){this.notes=n;}
    public LocalDate getPrescribedDate(){return prescribedDate;} public void setPrescribedDate(LocalDate d){this.prescribedDate=d;}
    public String getFollowUpDate(){return followUpDate;} public void setFollowUpDate(String f){this.followUpDate=f;}
    public List<PrescriptionItem> getMedicines(){return medicines;} public void setMedicines(List<PrescriptionItem> m){this.medicines=m;}
}
