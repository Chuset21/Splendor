package com.hexanome.fourteen.lobbyui;

import com.hexanome.fourteen.StagePayload;
import javafx.stage.Stage;

public class User {

    private String accessToken;
    private String refreshToken;
    private String userid;
    private Lobby currentLobby;

    public User(String userid){
        this.userid = userid;
        accessToken = "";
        refreshToken = "";
        currentLobby = null;
    }

    public User(String accessToken, String refreshToken, String userid){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userid = userid;
    }

    public static String getAccessToken(Stage stage){
        return ((StagePayload) stage.getUserData()).getUser().getAccessToken();
    }

    public static String getRefreshToken(Stage stage){
        return ((StagePayload) stage.getUserData()).getUser().getRefreshToken();
    }

    public static String getUserid(Stage stage){
        return ((StagePayload) stage.getUserData()).getUser().getUserid();
    }

    public static Lobby getCurrentLobby(Stage stage){
        return ((StagePayload) stage.getUserData()).getUser().getCurrentLobby();
    }

    public static User getUser(Stage stage){
        return ((StagePayload) stage.getUserData()).getUser();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUserid() {
        return userid;
    }

    public Lobby getCurrentLobby() {
        return currentLobby;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setCurrentLobby(Lobby currentLobby) {
        this.currentLobby = currentLobby;
    }
}
