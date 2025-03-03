package vn.edu.ngochandev.common;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@SuperBuilder
public abstract class PageResponse implements Serializable {
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
