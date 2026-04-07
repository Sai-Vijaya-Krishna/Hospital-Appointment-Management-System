package com.example.hospital.entity;
import jakarta.persistence.*;

@Entity @Table(name="doctors")
public class Doctor {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne @JoinColumn(name="user_id") private User user;
    @ManyToOne @JoinColumn(name="department_id") private Department department;
    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private Double consultationFee;
    private Double rating = 4.5;
    private String availableDays = "MON,TUE,WED,THU,FRI";
    private String availableFrom = "09:00";
    private String availableTo = "17:00";
    private Integer slotDurationMins = 30;
    private boolean available = true;

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public User getUser(){return user;} public void setUser(User u){this.user=u;}
    public Department getDepartment(){return department;} public void setDepartment(Department d){this.department=d;}
    public String getSpecialization(){return specialization;} public void setSpecialization(String s){this.specialization=s;}
    public String getQualification(){return qualification;} public void setQualification(String q){this.qualification=q;}
    public Integer getExperienceYears(){return experienceYears;} public void setExperienceYears(Integer e){this.experienceYears=e;}
    public Double getConsultationFee(){return consultationFee;} public void setConsultationFee(Double c){this.consultationFee=c;}
    public Double getRating(){return rating;} public void setRating(Double r){this.rating=r;}
    public String getAvailableDays(){return availableDays;} public void setAvailableDays(String a){this.availableDays=a;}
    public String getAvailableFrom(){return availableFrom;} public void setAvailableFrom(String a){this.availableFrom=a;}
    public String getAvailableTo(){return availableTo;} public void setAvailableTo(String a){this.availableTo=a;}
    public Integer getSlotDurationMins(){return slotDurationMins;} public void setSlotDurationMins(Integer s){this.slotDurationMins=s;}
    public boolean isAvailable(){return available;} public void setAvailable(boolean a){this.available=a;}
}
