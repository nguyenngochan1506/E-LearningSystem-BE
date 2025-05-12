package vn.edu.hcmuaf.fit.elearning.feature.courses.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmuaf.fit.elearning.common.BaseEntity;

@Entity
@Table(name = "tbl_quiz_answer")
@Getter
@Setter
public class QuizAnswerEntity extends BaseEntity {
    @Column(name = "content", length = 500)
    private String content;
    @Column(name = "is_correct")
    private Boolean isCorrect;
    @Column(name = "label", length = 2)
    private String label;
    @Column(name = "explanation", length = 500)
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private QuizQuestionEntity question;
}
