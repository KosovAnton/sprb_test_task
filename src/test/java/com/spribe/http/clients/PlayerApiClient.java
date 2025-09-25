package com.spribe.http.clients;

import com.spribe.model.player.request.DeletePlayerRequest;
import com.spribe.model.player.request.GetPlayerByIdRequest;
import com.spribe.model.player.request.UpdatePlayerRequest;
import com.spribe.model.player.response.CreatePlayerResponse;
import io.restassured.response.Response;

import static com.spribe.cleanup.CreatedPlayersRegistryPerTest.register;
import static io.restassured.RestAssured.given;

public class PlayerApiClient {
    public Response getPlayerById(int id) {
        return given()
                .body(new GetPlayerByIdRequest(id))
                .post("player/get");
    }

    public Response getAllPlayers() {
        Response r = given()
                .log().all()            // лог запроса в консоль
                .when().get("player/get/all")
                .then().log().all()     // лог ответа в консоль (из этого же r)
                .extract().response();

        System.out.println("--- asString():\n" + r.asString());

        return r;
    }

    public Response createPlayer(String editor,
                                 int age,
                                 String gender,
                                 String login,
                                 String password,
                                 String role,
                                 String screenName) {
        Response r = given()
                .param("age", age)
                .param("gender", gender)
                .param("login", login)
                .param("password", password)
                .param("role", role)
                .param("screenName", screenName)
                .get("player/create/" + editor);

        if (r.statusCode() == 200) {
            try{
                var id = r.as(CreatePlayerResponse.class).id();
                register(id);
            } catch (Exception ignore) {
            }
        }

        return r;
    }

    public Response deletePlayer(String editor, int id) {
        return given()
                .body(new DeletePlayerRequest(id))
                .delete("player/delete/" + editor);
    }

    public Response updatePlayer(String editor, int id, UpdatePlayerRequest body) {
        return given()
                .body(body)
                .patch("player/update/" + editor + "/" + id);
    }
}
