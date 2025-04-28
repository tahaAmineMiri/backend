package com.incon.backend.dto.request;

public class AuthRequest {
    private String accessToken;
    private String refreshToken;
    private Integer accessTokenTTL; // Add field
    private Integer refreshTokenTTL; // Add field

    // Getters and setters
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public Integer getAccessTokenTTL() { return accessTokenTTL; }
    public void setAccessTokenTTL(Integer accessTokenTTL) { this.accessTokenTTL = accessTokenTTL; }
    public Integer getRefreshTokenTTL() { return refreshTokenTTL; }
    public void setRefreshTokenTTL(Integer refreshTokenTTL) { this.refreshTokenTTL = refreshTokenTTL; }
}