package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Assessment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Assessment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {}
