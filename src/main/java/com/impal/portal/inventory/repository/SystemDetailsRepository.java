package com.impal.portal.inventory.repository;

import com.impal.portal.inventory.entity.SystemDetails;
import com.impal.portal.branch.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemDetailsRepository
        extends JpaRepository<SystemDetails, Long> {

    boolean existsBySerialNumber(String serialNumber);

    List<SystemDetails> findByBranch(Branch branch);
}
