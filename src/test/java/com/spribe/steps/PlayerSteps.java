package com.spribe.steps;

import com.spribe.http.clients.PlayerApiClient;
import com.spribe.model.player.request.UpdatePlayerRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class PlayerSteps {
    private final PlayerApiClient api = new PlayerApiClient();

    @Step("Get player by id={id}")
    public Response getPlayerById(int id) {
        return api.getPlayerById(id);
    }

    @Step("Get all players")
    public Response getAllPlayers() {
        return api.getAllPlayers();
    }

    @Step("Create player editor={editor}, age={age}, gender={gender}, login={login}, password={password}, role={role}, screenName={screenName}")
    public Response createPlayer(String editor,
                                 int age,
                                 String gender,
                                 String login,
                                 String password,
                                 String role,
                                 String screenName) {
        return api.createPlayer(editor, age, gender, login, password, role, screenName);
    }

    @Step("Delete player by editor={editor}, id={id}")
    public Response deletePlayer(String editor, int id) {
        return api.deletePlayer(editor, id);
    }

    @Step("Update player editor={editor}, id={id}, age={age}, gender={gender}, login={login}, password={password}, role={role}, screenName={screenName}")
    public Response updatePlayer(String editor,
                                 int id,
                                 int age,
                                 String gender,
                                 String login,
                                 String password,
                                 String role,
                                 String screenName) {
        var body = UpdatePlayerRequest.builder()
                .age(age)
                .gender(gender)
                .login(login)
                .password(password)
                .role(role)
                .screenName(screenName)
                .build();
        return api.updatePlayer(editor, id, body);
    }

    @Step("Update player editor={editor}, id={id}, body={body}")
    public Response updatePlayer(String editor, int id, UpdatePlayerRequest body) {
        return api.updatePlayer(editor, id, body);
    }
}
