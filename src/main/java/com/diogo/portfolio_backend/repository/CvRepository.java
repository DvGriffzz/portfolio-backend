package com.diogo.portfolio_backend.repository;

import com.diogo.portfolio_backend.entity.CvFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CvRepository extends JpaRepository<CvFile, Long> {

}
