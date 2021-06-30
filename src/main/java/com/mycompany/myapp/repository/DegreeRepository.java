package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Degree;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Degree entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {}
