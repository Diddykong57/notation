package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Get.
 */
@Entity
@Table(name = "get")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Get implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    @Column(name = "note", nullable = false)
    private Float note;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gets", "subject" }, allowSetters = true)
    private Assessment assessment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "gets", "degree" }, allowSetters = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Get id(Long id) {
        this.id = id;
        return this;
    }

    public Float getNote() {
        return this.note;
    }

    public Get note(Float note) {
        this.note = note;
        return this;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public Assessment getAssessment() {
        return this.assessment;
    }

    public Get assessment(Assessment assessment) {
        this.setAssessment(assessment);
        return this;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public Student getStudent() {
        return this.student;
    }

    public Get student(Student student) {
        this.setStudent(student);
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Get)) {
            return false;
        }
        return id != null && id.equals(((Get) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Get{" +
            "id=" + getId() +
            ", note=" + getNote() +
            "}";
    }
}
