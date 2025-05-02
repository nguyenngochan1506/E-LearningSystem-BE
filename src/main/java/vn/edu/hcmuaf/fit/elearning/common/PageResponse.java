package vn.edu.hcmuaf.fit.elearning.common;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@SuperBuilder
public class PageResponse implements Serializable {
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
