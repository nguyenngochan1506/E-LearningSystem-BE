package vn.edu.hcmuaf.fit.elearning.feature.courses.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmuaf.fit.elearning.common.BaseEntity;
import vn.edu.hcmuaf.fit.elearning.common.enums.QuestionType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_quiz_question")
@Getter
@Setter
public class QuizQuestionEntity extends BaseEntity {
    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "content", length = 500)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(name = "points", length = 500)
    private float points;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private QuizEntity quiz;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<QuizAnswerEntity> answers = new HashSet<>();
}
