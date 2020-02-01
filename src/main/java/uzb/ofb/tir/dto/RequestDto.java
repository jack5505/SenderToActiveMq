package uzb.ofb.tir.dto;

public class RequestDto {
    private final Long id;
    private final String header;

    public RequestDto(Long id, String header) {
        this.id = id;
        this.header = header;
    }

    public Long getId() {
        return id;
    }

}
