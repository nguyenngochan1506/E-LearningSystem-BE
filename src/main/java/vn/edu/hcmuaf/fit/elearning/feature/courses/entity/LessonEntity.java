package vn.edu.hcmuaf.fit.elearning.feature.courses.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmuaf.fit.elearning.common.BaseEntity;
@Entity
@Table(name = "tbl_lesson")
@Getter
@Setter
public class LessonEntity extends BaseEntity {
    @Column(name = "number")
    private Integer number;
    @Column(name = "name")
    private String name;
    @Column(name = "content")
    private String content;
    @Column(name = "video_url", length = 500)
    private String videoUrl;
    @Column(name = "file_url", length = 500)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "module_id", referencedColumnName = "id")
    private ModuleEntity module;
}
