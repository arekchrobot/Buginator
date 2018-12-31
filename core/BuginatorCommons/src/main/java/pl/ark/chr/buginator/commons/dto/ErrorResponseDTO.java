package pl.ark.chr.buginator.commons.dto;

public class ErrorResponseDTO {

    private String message;

    public ErrorResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
