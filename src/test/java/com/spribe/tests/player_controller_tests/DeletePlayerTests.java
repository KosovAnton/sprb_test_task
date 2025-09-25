package com.spribe.tests.player_controller_tests;

import com.spribe.helpers.StringGeneratorHelper;
import com.spribe.model.player.response.CreatePlayerResponse;
import com.spribe.model.player.response.GetAllPlayersResponse;
import com.spribe.steps.PlayerSteps;
import com.spribe.tests.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

import static com.spribe.enums.Gender.MALE;
import static com.spribe.enums.Role.*;

@Epic("Player controller")
@Feature("Delete player")
public class DeletePlayerTests extends BaseTest {
    private final PlayerSteps steps = new PlayerSteps();

    @Test(description = "Validate SUPERVISOR can delete ADMIN")
    @Description("""
            **Test:**
            - create ADMIN
            - delete ADMIN
            **Validate:**
            - status code 204
            - player does not exist in get all players response list
            """)
    public void validateSupervisorCanDeleteAdmin() {
        AtomicReference<Response> response = new AtomicReference<>();
        Allure.step("Create ADMIN with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            response.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    ADMIN.getRole(),
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
    }

    @Test(description = "Validate SUPERVISOR can delete USER")
    @Description("""
            **Test:**
            - create USER
            - delete USER
            **Validate:**
            - status code 204
            - player does not exist in get all players response list
            """)
    public void validateSupervisorCanDeleteUser() {
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
    }

    @Test(description = "Validate ADMIN can delete USER")
    @Description("""
            **Test:**
            - create ADMIN
            - create USER
            - delete USER
            **Validate:**
            - status code 204
            - player does not exist in get all players response list
            """)
    public void validateAdminCanDeleteUser() {
        AtomicReference<Response> createAdminResponse = new AtomicReference<>();
        Allure.step("Create ADMIN with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            createAdminResponse.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    ADMIN.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createAdminResponse.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> createUserResponse = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            createUserResponse.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    USER.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createUserResponse.get().statusCode(), 200, "status code");
        });

        var createdUserId = createUserResponse.get().as(CreatePlayerResponse.class).id();
        Allure.step("Delete player with id " + createdUserId, () -> {
            var adminEditor = createAdminResponse.get().as(CreatePlayerResponse.class).login();
            Response deleteResp = steps.deletePlayer(adminEditor, createdUserId);
            Assert.assertEquals(deleteResp.statusCode(), 204, "status code");
        });

        Allure.step("Validate all players list does not contain player with deleted id " + createdUserId, () -> {
            var players = steps.getAllPlayers().as(GetAllPlayersResponse.class).players();
            Assert.assertFalse(players.isEmpty(), "players list is not empty");
            var idNotExist = players.stream().noneMatch(player -> player.id().equals(createdUserId));
            Assert.assertTrue(idNotExist, "id " + createdUserId + " not exist");
        });
    }

    @Test(description = "Validate USER cannot delete ADMIN")
    @Description("""
            **Test:**
            - create ADMIN
            - create USER
            - delete ADMIN with editor USER
            **Validate:**
            - status code 403
            - player exist in get all players response list
            """)
    public void validateUserCannotDeleteAdmin() {
        AtomicReference<Response> createAdminResponse = new AtomicReference<>();
        Allure.step("Create ADMIN with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            createAdminResponse.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    ADMIN.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createAdminResponse.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> createUserResponse = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            createUserResponse.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    USER.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createUserResponse.get().statusCode(), 200, "status code");
        });

        var createdAdminId = createAdminResponse.get().as(CreatePlayerResponse.class).id();
        var adminEditor = createAdminResponse.get().as(CreatePlayerResponse.class).login();
        Response deleteResp = steps.deletePlayer(adminEditor, createdAdminId);

        Allure.step("Validate status code 403", () -> {
            Assert.assertEquals(deleteResp.statusCode(), 403, "status code");
        });

        Allure.step("Validate all players list contain player with id " + createdAdminId, () -> {
            var players = steps.getAllPlayers().as(GetAllPlayersResponse.class).players();
            Assert.assertFalse(players.isEmpty(), "players list is not empty");
            var exist = players.stream().anyMatch(player -> player.id().equals(createdAdminId));
            Assert.assertTrue(exist, "id " + createdAdminId + " exist");
        });
    }

    @Test(description = "Validate SUPERVISOR can delete USER")
    @Description("""
            **Test:**
            - create USER
            - delete USER
            - try delete USER with same id
            **Validate:**
            - status code 404 Not Found
            """)
    public void validateDeletePlayerEndpointWithNonExistingId() {
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

        Response deleteResp = steps.deletePlayer(SUPERVISOR.getRole(), id);

        Allure.step("Validate status code 404", () -> {
            Assert.assertEquals(deleteResp.statusCode(), 404, "status code");
        });
    }

    @Test(description = "Validate USER cannot be deleted by itself as editor")
    @Description("""
            **Test:**
            - create USER
            - delete USER
            - try delete USER with same id
            **Validate:**
            - status code 404 Not Found
            """)
    public void validateUserCannotBeDeletedByItselfAsEditor() {
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

        var respBody = response.get().as(CreatePlayerResponse.class);
        var id = respBody.id();
        var login = respBody.login();
        Response deleteResp = steps.deletePlayer(login, id);

        Allure.step("Validate status code 403", () -> {
            Assert.assertEquals(deleteResp.statusCode(), 403, "status code");
        });
    }
}
