package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TypeAssessment;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Assessment.
 */
@Entity
@Table(name = "assessment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Assessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "coef_cont")
    private Integer coefCont;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeAssessment type;

    @OneToMany(mappedBy = "assessment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "assessment", "student" }, allowSetters = true)
    private Set<Get> gets = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "assessments", "degree" }, allowSetters = true)
    private Subject subject;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Assessment id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Assessment date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getCoefCont() {
        return this.coefCont;
    }

    public Assessment coefCont(Integer coefCont) {
        this.coefCont = coefCont;
        return this;
    }

    public void setCoefCont(Integer coefCont) {
        this.coefCont = coefCont;
    }

    public TypeAssessment getType() {
        return this.type;
    }

    public Assessment type(TypeAssessment type) {
        this.type = type;
        return this;
    }

    public void setType(TypeAssessment type) {
        this.type = type;
    }

    public Set<Get> getGets() {
        return this.gets;
    }

    public Assessment gets(Set<Get> gets) {
        this.setGets(gets);
        return this;
    }

    public Assessment addGet(Get get) {
        this.gets.add(get);
        get.setAssessment(this);
        return this;
    }

    public Assessment removeGet(Get get) {
        this.gets.remove(get);
        get.setAssessment(null);
        return this;
    }

    public void setGets(Set<Get> gets) {
        if (this.gets != null) {
            this.gets.forEach(i -> i.setAssessment(null));
        }
        if (gets != null) {
            gets.forEach(i -> i.setAssessment(this));
        }
        this.gets = gets;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public Assessment subject(Subject subject) {
        this.setSubject(subject);
        return this;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assessment)) {
            return false;
        }
        return id != null && id.equals(((Assessment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assessment{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", coefCont=" + getCoefCont() +
            ", type='" + getType() + "'" +
            "}";
    }
}
