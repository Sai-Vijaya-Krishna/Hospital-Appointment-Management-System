package com.example.hospital.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity @Table(name="prescription_items")
public class PrescriptionItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne @JoinColumn(name="prescription_id") @JsonBackReference private Prescription prescription;
    private String medicineName;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Prescription getPrescription(){return prescription;} public void setPrescription(Prescription p){this.prescription=p;}
    public String getMedicineName(){return medicineName;} public void setMedicineName(String m){this.medicineName=m;}
    public String getDosage(){return dosage;} public void setDosage(String d){this.dosage=d;}
    public String getFrequency(){return frequency;} public void setFrequency(String f){this.frequency=f;}
    public String getDuration(){return duration;} public void setDuration(String d){this.duration=d;}
    public String getInstructions(){return instructions;} public void setInstructions(String i){this.instructions=i;}
}
