package re.edu.identityservice.dto;

public record RegisterRequest(
        String username,
        String password
) {
}