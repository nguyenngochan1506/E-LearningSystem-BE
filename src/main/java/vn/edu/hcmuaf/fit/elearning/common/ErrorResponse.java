package vn.edu.hcmuaf.fit.elearning.common;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Builder
public class ErrorResponse implements Serializable {
    private Date timestamp;
    private int status;
    private String error;
    private String path;
    private String message;

    public String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSSS")
                .withZone(ZoneId.systemDefault());
        return formatter.format(timestamp.toInstant());
    }
}
