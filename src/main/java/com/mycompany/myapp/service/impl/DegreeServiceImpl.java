package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Degree;
import com.mycompany.myapp.repository.DegreeRepository;
import com.mycompany.myapp.service.DegreeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Degree}.
 */
@Service
@Transactional
public class DegreeServiceImpl implements DegreeService {

    private final Logger log = LoggerFactory.getLogger(DegreeServiceImpl.class);

    private final DegreeRepository degreeRepository;

    public DegreeServiceImpl(DegreeRepository degreeRepository) {
        this.degreeRepository = degreeRepository;
    }

    @Override
    public Degree save(Degree degree) {
        log.debug("Request to save Degree : {}", degree);
        return degreeRepository.save(degree);
    }

    @Override
    public Optional<Degree> partialUpdate(Degree degree) {
        log.debug("Request to partially update Degree : {}", degree);

        return degreeRepository
            .findById(degree.getId())
            .map(
                existingDegree -> {
                    if (degree.getNameDipl() != null) {
                        existingDegree.setNameDipl(degree.getNameDipl());
                    }

                    return existingDegree;
                }
            )
            .map(degreeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Degree> findAll() {
        log.debug("Request to get all Degrees");
        return degreeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Degree> findOne(Long id) {
        log.debug("Request to get Degree : {}", id);
        return degreeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Degree : {}", id);
        degreeRepository.deleteById(id);
    }
}
