package vn.edu.hcmuaf.fit.elearning.feature.courses.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmuaf.fit.elearning.common.BaseEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_module")
@Getter
@Setter
public class ModuleEntity extends BaseEntity {
    @Column(name = "number")
    private Integer number;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private CourseEntity course;

    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LessonEntity> lessons = new HashSet<>();

    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<QuizEntity> quizzes = new HashSet<>();
}
