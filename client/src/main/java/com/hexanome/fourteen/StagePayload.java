package com.hexanome.fourteen;

import com.hexanome.fourteen.lobbyui.MenuController;
import com.hexanome.fourteen.lobbyui.User;
import javafx.stage.Stage;

public class StagePayload {

    private User user;
    private MenuController menuController;
    private Object payload;

    public StagePayload(){
        user = null;
        menuController = null;
        payload = null;
    }

    public StagePayload(MenuController menuController){
        user = null;
        this.menuController = menuController;
        payload = null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public User getUser() {
        return user;
    }

    public MenuController getMenuController() {
        return menuController;
    }

    public Object getPayload() {
        return payload;
    }
}
