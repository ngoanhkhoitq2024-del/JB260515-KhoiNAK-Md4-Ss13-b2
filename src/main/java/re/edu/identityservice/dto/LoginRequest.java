package re.edu.identityservice.dto;

public record LoginRequest(
        String username,
        String password
) {
}
