package vn.edu.hcmuaf.fit.elearning.feature.courses.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmuaf.fit.elearning.common.BaseEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_quiz")
@Getter
@Setter
public class QuizEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "time_limit")
    private Integer timeLimit;

    @Column(name = "max_attempt")
    private Integer maxAttempt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    private ModuleEntity module;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<QuizQuestionEntity> questions = new HashSet<>();
}
