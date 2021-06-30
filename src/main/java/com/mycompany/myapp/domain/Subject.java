package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Subject.
 */
@Entity
@Table(name = "subject")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name_mat")
    private String nameMat;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "coef_mat")
    private Integer coefMat;

    @OneToMany(mappedBy = "subject")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "gets", "subject" }, allowSetters = true)
    private Set<Assessment> assessments = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "students", "subjects" }, allowSetters = true)
    private Degree degree;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Subject id(Long id) {
        this.id = id;
        return this;
    }

    public String getNameMat() {
        return this.nameMat;
    }

    public Subject nameMat(String nameMat) {
        this.nameMat = nameMat;
        return this;
    }

    public void setNameMat(String nameMat) {
        this.nameMat = nameMat;
    }

    public Integer getCoefMat() {
        return this.coefMat;
    }

    public Subject coefMat(Integer coefMat) {
        this.coefMat = coefMat;
        return this;
    }

    public void setCoefMat(Integer coefMat) {
        this.coefMat = coefMat;
    }

    public Set<Assessment> getAssessments() {
        return this.assessments;
    }

    public Subject assessments(Set<Assessment> assessments) {
        this.setAssessments(assessments);
        return this;
    }

    public Subject addAssessment(Assessment assessment) {
        this.assessments.add(assessment);
        assessment.setSubject(this);
        return this;
    }

    public Subject removeAssessment(Assessment assessment) {
        this.assessments.remove(assessment);
        assessment.setSubject(null);
        return this;
    }

    public void setAssessments(Set<Assessment> assessments) {
        if (this.assessments != null) {
            this.assessments.forEach(i -> i.setSubject(null));
        }
        if (assessments != null) {
            assessments.forEach(i -> i.setSubject(this));
        }
        this.assessments = assessments;
    }

    public Degree getDegree() {
        return this.degree;
    }

    public Subject degree(Degree degree) {
        this.setDegree(degree);
        return this;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subject)) {
            return false;
        }
        return id != null && id.equals(((Subject) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subject{" +
            "id=" + getId() +
            ", nameMat='" + getNameMat() + "'" +
            ", coefMat=" + getCoefMat() +
            "}";
    }
}
