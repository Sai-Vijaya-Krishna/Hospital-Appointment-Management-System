package com.example.hospital.repository;
import com.example.hospital.entity.Doctor;
import com.example.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface DoctorRepository extends JpaRepository<Doctor,Long> {
    List<Doctor> findByDepartmentIdAndAvailableTrue(Long deptId);
    List<Doctor> findByAvailableTrue();
    Optional<Doctor> findByUser(User user);
}
