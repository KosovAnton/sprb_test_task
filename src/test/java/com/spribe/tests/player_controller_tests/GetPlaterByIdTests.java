package com.spribe.tests.player_controller_tests;

import com.spribe.helpers.StringGeneratorHelper;
import com.spribe.model.player.response.CreatePlayerResponse;
import com.spribe.model.player.response.GetAllPlayersResponse;
import com.spribe.model.player.response.GetPlayerByIdResponse;
import com.spribe.steps.PlayerSteps;
import com.spribe.tests.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.concurrent.atomic.AtomicReference;

import static com.spribe.enums.Gender.MALE;
import static com.spribe.enums.Role.*;

@Epic("Player controller")
@Feature("Get player by id")
public class GetPlaterByIdTests extends BaseTest {
    private final PlayerSteps steps = new PlayerSteps();

    @Test(description = "Validate that created player can be found by id")
    @Description("""
            **Test:**
            - create player
            - get id from response body
            - execute POST /player/get 
            **Validate:**
            - player created
            - status code 200
            - POST /player/get return correct player
            - status code 200
            - validate response body
            """)
    public void validateGetPlayerByIdReturnCorrectPlayer() {
        AtomicReference<Response> response = new AtomicReference<>();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword();
        var screenName = StringGeneratorHelper.generateScreenName();
        Integer age = 17;
        var gender = MALE.getGender();
        var role = ADMIN.getRole();
        Allure.step("Create ADMIN with editor SUPERVISOR", () -> {
            response.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(response.get().statusCode(), 200, "status code");
        });

        AtomicReference<CreatePlayerResponse> createPlayerResponse = new AtomicReference<>();
        Allure.step("Validate response body properties: id, login", () -> {
            createPlayerResponse.set(response.get().as(CreatePlayerResponse.class));
            Assert.assertNotNull(createPlayerResponse.get().id(), "id");
            Assert.assertEquals(createPlayerResponse.get().login(), login, "login");
        });

        Allure.step("Validate POST /player/get return correct player", () -> {
            var id = createPlayerResponse.get().id();
            Response getPlayerByIdResponse = steps.getPlayerById(id);
            Assert.assertEquals(getPlayerByIdResponse.getStatusCode(), 200, "status code");

            var responseBody = getPlayerByIdResponse.as(GetPlayerByIdResponse.class);
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertEquals(responseBody.id(), id, "id");
            softAssert.assertEquals(responseBody.age(), age, "age");
            softAssert.assertEquals(responseBody.gender(), gender, "gender");
            softAssert.assertEquals(responseBody.login(), login, "login");
            softAssert.assertEquals(responseBody.role(), role, "role");
            softAssert.assertEquals(responseBody.screenName(), screenName, "screenName");
            softAssert.assertEquals(responseBody.password(), password, "password");
            softAssert.assertAll();
        });
    }

    @Test(description = "Validate get player by id with deleted player id")
    @Description("""
            **Test:**
            - create player
            - get id from response body
            - execute DELETE /player/player/{editor}
            - validate player does not exist in get all players response list
            - execute POST /player/get
            **Validate:**
            - status code 404 Not Found
            """)
    public void validateGetPlayerByIdForDeletedPlayer() {
        AtomicReference<Response> response = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            response.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    USER.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(response.get().statusCode(), 200, "status code");
        });

        var id = response.get().as(CreatePlayerResponse.class).id();
        Allure.step("Delete player with id " + id, () -> {
            Response deleteResp = steps.deletePlayer(SUPERVISOR.getRole(), id);
            Assert.assertEquals(deleteResp.statusCode(), 204, "status code");
        });

        Allure.step("Validate all players list does not contain player with deleted id " + id, () -> {
            var players = steps.getAllPlayers().as(GetAllPlayersResponse.class).players();
            Assert.assertFalse(players.isEmpty(), "players list is not empty");
            var idNotExist = players.stream().noneMatch(player -> player.id().equals(id));
            Assert.assertTrue(idNotExist, "id " + id + " not exist");
        });

        Response getByIdResp = steps.getPlayerById(id);

        Allure.step("Validate status code 404", () -> {
            Assert.assertEquals(getByIdResp.statusCode(), 404, "status code");
        });
    }
}
