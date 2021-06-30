package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Degree.
 */
@Entity
@Table(name = "degree")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Degree implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name_dipl")
    private String nameDipl;

    @OneToMany(mappedBy = "degree")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "gets", "degree" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "degree")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "assessments", "degree" }, allowSetters = true)
    private Set<Subject> subjects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Degree id(Long id) {
        this.id = id;
        return this;
    }

    public String getNameDipl() {
        return this.nameDipl;
    }

    public Degree nameDipl(String nameDipl) {
        this.nameDipl = nameDipl;
        return this;
    }

    public void setNameDipl(String nameDipl) {
        this.nameDipl = nameDipl;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public Degree students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Degree addStudent(Student student) {
        this.students.add(student);
        student.setDegree(this);
        return this;
    }

    public Degree removeStudent(Student student) {
        this.students.remove(student);
        student.setDegree(null);
        return this;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.setDegree(null));
        }
        if (students != null) {
            students.forEach(i -> i.setDegree(this));
        }
        this.students = students;
    }

    public Set<Subject> getSubjects() {
        return this.subjects;
    }

    public Degree subjects(Set<Subject> subjects) {
        this.setSubjects(subjects);
        return this;
    }

    public Degree addSubject(Subject subject) {
        this.subjects.add(subject);
        subject.setDegree(this);
        return this;
    }

    public Degree removeSubject(Subject subject) {
        this.subjects.remove(subject);
        subject.setDegree(null);
        return this;
    }

    public void setSubjects(Set<Subject> subjects) {
        if (this.subjects != null) {
            this.subjects.forEach(i -> i.setDegree(null));
        }
        if (subjects != null) {
            subjects.forEach(i -> i.setDegree(this));
        }
        this.subjects = subjects;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Degree)) {
            return false;
        }
        return id != null && id.equals(((Degree) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Degree{" +
            "id=" + getId() +
            ", nameDipl='" + getNameDipl() + "'" +
            "}";
    }
}
