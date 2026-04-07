package com.example.hospital.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity @Table(name="users")
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false,unique=true) private String email;
    @Column(nullable=false) @JsonIgnore private String password;
    private String phone;
    private String bloodGroup;
    private Integer age;
    @Column(nullable=false) private String role = "PATIENT";

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String n){this.name=n;}
    public String getEmail(){return email;} public void setEmail(String e){this.email=e;}
    public String getPassword(){return password;} public void setPassword(String p){this.password=p;}
    public String getPhone(){return phone;} public void setPhone(String p){this.phone=p;}
    public String getBloodGroup(){return bloodGroup;} public void setBloodGroup(String b){this.bloodGroup=b;}
    public Integer getAge(){return age;} public void setAge(Integer a){this.age=a;}
    public String getRole(){return role;} public void setRole(String r){this.role=r;}
}
