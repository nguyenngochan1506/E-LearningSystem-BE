package vn.edu.hcmuaf.fit.elearning.feature.file;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.edu.hcmuaf.fit.elearning.common.BaseEntity;

@Entity
@Table(name = "tbl_file")
@Getter
@Setter
public class FileEntity extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "url", length = 500)
    private String url;
    @Column(name = "size")
    private Long size;
    @Column(name = "type")
    private String type;
}
