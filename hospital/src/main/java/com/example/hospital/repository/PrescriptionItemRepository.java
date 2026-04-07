package com.example.hospital.repository;

import com.example.hospital.entity.PrescriptionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {

    // ✅ Needed to clear old medicines before saving updated ones
    @Modifying
    @Transactional
    @Query("DELETE FROM PrescriptionItem pi WHERE pi.prescription.id = :prescriptionId")
    void deleteByPrescriptionId(Long prescriptionId);
}