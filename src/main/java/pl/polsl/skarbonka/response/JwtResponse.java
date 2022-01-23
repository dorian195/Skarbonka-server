package pl.polsl.skarbonka.response;

public class JwtResponse {
    private final String role;
    private String token;
    private String id;
    private String username;

    public JwtResponse(String accessToken, String id, String username, String role) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
