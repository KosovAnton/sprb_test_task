package com.spribe.tests.player_controller_tests;

import com.spribe.dataproviders.PasswordDataProvider;
import com.spribe.helpers.StringGeneratorHelper;
import com.spribe.model.player.request.UpdatePlayerRequest;
import com.spribe.model.player.response.CreatePlayerResponse;
import com.spribe.model.player.response.GetPlayerByIdResponse;
import com.spribe.model.player.response.UpdatePlayerResponse;
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

import static com.spribe.enums.Gender.FEMALE;
import static com.spribe.enums.Gender.MALE;
import static com.spribe.enums.Role.*;

@Epic("Player controller")
@Feature("Update player")
public class UpdatePlayerTests extends BaseTest {
    private final PlayerSteps steps = new PlayerSteps();

    @Test(description = "Validate that ADMIN can be updated with editor - SUPERVISOR")
    @Description("""
            **Validate:**
            - ADMIN can be updated with editor - SUPERVISOR
            - status code 200
            - validate response body
            """)
    public void validateAdminUpdatedWithSupervisorEditor() {
        AtomicReference<Response> createResponse = new AtomicReference<>();
        Allure.step("Create ADMIN with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            int age = 17;
            var gender = MALE.getGender();
            var role = ADMIN.getRole();
            createResponse.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createResponse.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> updateResponse = new AtomicReference<>();
        var id = createResponse.get().as(CreatePlayerResponse.class).id();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword();
        var screenName = StringGeneratorHelper.generateScreenName();
        Integer age = 18;
        var gender = FEMALE.getGender();
        var role = USER.getRole();
        Allure.step("Update ADMIN with editor SUPERVISOR", () -> {
            updateResponse.set(steps.updatePlayer(
                    SUPERVISOR.getRole(),
                    id,
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(updateResponse.get().statusCode(), 200, "status code");
        });

        Allure.step("Validate response body", () -> {
            var responseBody = updateResponse.get().as(UpdatePlayerResponse.class);
            SoftAssert softAssert = new SoftAssert();
            softAssert.assertNotNull(responseBody.id(), "id");
            softAssert.assertEquals(responseBody.login(), login, "login");
            softAssert.assertEquals(responseBody.screenName(), screenName, "screenName");
            softAssert.assertEquals(responseBody.gender(), gender, "gender");
            softAssert.assertEquals(responseBody.age(), age, "age");
            softAssert.assertEquals(responseBody.role(), role, "role");
            softAssert.assertAll();
        });
    }

    @Test(description = "Validate that player age cannot be updated to age 16")
    @Description("""
            **Test:**
            - Create USER with age 17
            - Update USER age to 16
            **Validate:**
            - status code 400 - Bad Request
            """)
    public void validatePlayerAgeUpdateTo16() {
        AtomicReference<Response> createResponse = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            int age = 17;
            var gender = MALE.getGender();
            var role = USER.getRole();
            createResponse.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createResponse.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> updateResponse = new AtomicReference<>();
        var id = createResponse.get().as(CreatePlayerResponse.class).id();
        Allure.step("Update ADMIN with editor SUPERVISOR", () -> {
            var requestBody = UpdatePlayerRequest.builder()
                    .age(16)
                    .build();
            updateResponse.set(steps.updatePlayer(SUPERVISOR.getRole(), id, requestBody));
        });

        Allure.step("Validate status code 400", () -> {
            Assert.assertEquals(updateResponse.get().statusCode(), 400, "status code");
        });
    }

    @Test(description = "Validate that player age cannot be updated to age 60")
    @Description("""
            **Test:**
            - Create USER with age 17
            - Update USER age to 60
            **Validate:**
            - status code 400 - Bad Request
            """)
    public void validatePlayerAgeUpdateTo60() {
        AtomicReference<Response> createResponse = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            int age = 17;
            var gender = MALE.getGender();
            var role = USER.getRole();
            createResponse.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createResponse.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> updateResponse = new AtomicReference<>();
        var id = createResponse.get().as(CreatePlayerResponse.class).id();
        Allure.step("Update ADMIN with editor SUPERVISOR", () -> {
            var requestBody = UpdatePlayerRequest.builder()
                    .age(60)
                    .build();
            updateResponse.set(steps.updatePlayer(SUPERVISOR.getRole(), id, requestBody));
        });

        Allure.step("Validate status code 400", () -> {
            Assert.assertEquals(updateResponse.get().statusCode(), 400, "status code");
        });
    }

    @Test(description = "Validate that player gender cannot be updated invalid value")
    @Description("""
            **Test:**
            - Create USER gender MALE
            - Update USER gender MAIL
            **Validate:**
            - status code 400 - Bad Request
            """)
    public void validatePlayerGenderUpdateToInvalidValue() {
        AtomicReference<Response> createResponse = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            int age = 17;
            var gender = MALE.getGender();
            var role = USER.getRole();
            createResponse.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createResponse.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> updateResponse = new AtomicReference<>();
        var id = createResponse.get().as(CreatePlayerResponse.class).id();
        Allure.step("Update ADMIN with editor SUPERVISOR", () -> {
            var requestBody = UpdatePlayerRequest.builder()
                    .gender("mail")
                    .build();
            updateResponse.set(steps.updatePlayer(SUPERVISOR.getRole(), id, requestBody));
        });

        Allure.step("Validate status code 400", () -> {
            Assert.assertEquals(updateResponse.get().statusCode(), 400, "status code");
        });
    }

    @Test(description = "Validate that player login cannot be updated to existing one")
    @Description("""
            **Test:**
            - Create two players
            - update second one with login of first player
            **Validate:**
            - status code 409 - Conflict
            """)
    public void validateThatPlayerLoginCannotBeUpdatedToExistingOne() {
        AtomicReference<Response> createResponse1 = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            int age = 17;
            var gender = MALE.getGender();
            var role = USER.getRole();
            createResponse1.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createResponse1.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> createResponse2 = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            int age = 17;
            var gender = MALE.getGender();
            var role = USER.getRole();
            createResponse2.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createResponse2.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> updateResponse = new AtomicReference<>();
        var id = createResponse2.get().as(CreatePlayerResponse.class).id();
        var existingLogin = createResponse1.get().as(CreatePlayerResponse.class).login();
        Allure.step("Update ADMIN with editor SUPERVISOR", () -> {
            var requestBody = UpdatePlayerRequest.builder()
                    .login(existingLogin)
                    .build();
            updateResponse.set(steps.updatePlayer(SUPERVISOR.getRole(), id, requestBody));
        });

        Allure.step("Validate status code 409", () -> {
            Assert.assertEquals(updateResponse.get().statusCode(), 409, "status code");
        });
    }

    @Test(description = "Validate that player screenName cannot be updated to existing one")
    @Description("""
            **Test:**
            - Create two players
            - update second one with screenName of first player
            **Validate:**
            - status code 409 - Conflict
            """)
    public void validateThatPlayerScreenNameCannotBeUpdatedToExistingOne() {
        AtomicReference<Response> createResponse1 = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = "first_" + StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = "first_" + StringGeneratorHelper.generateScreenName();
            int age = 17;
            var gender = MALE.getGender();
            var role = USER.getRole();
            createResponse1.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createResponse1.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> createResponse2 = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = "second_" + StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = "second_" + StringGeneratorHelper.generateScreenName();
            int age = 17;
            var gender = MALE.getGender();
            var role = USER.getRole();
            createResponse2.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createResponse2.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> updateResponse = new AtomicReference<>();
        Allure.step("Update second player with first player screenName", () -> {
            var firstPlayerId = createResponse1.get().as(CreatePlayerResponse.class).id();
            var firstPlayerScreenName = steps.getPlayerById(firstPlayerId).as(GetPlayerByIdResponse.class).screenName();
            var secondPlayerId = createResponse2.get().as(CreatePlayerResponse.class).id();

            var requestBody = UpdatePlayerRequest.builder()
                    .screenName(firstPlayerScreenName)
                    .build();
            updateResponse.set(steps.updatePlayer(SUPERVISOR.getRole(), secondPlayerId, requestBody));
        });

        Allure.step("Validate status code 409", () -> {
            Assert.assertEquals(updateResponse.get().statusCode(), 409, "status code");
        });
    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = PasswordDataProvider.class,
            description = "Validate that player cannot be updated with invalid password")
    @Description("""
            **Test:**
            - Create player
            - Update player password with invalid value
            **Validate:**
            - status code 400 - Bad Request
            """)
    public void validateUserNotCreatedWithSupervisorEditorWithInvalidPassword(String invalidPassword) {
        AtomicReference<Response> createResponse = new AtomicReference<>();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            int age = 17;
            var gender = MALE.getGender();
            var role = USER.getRole();
            createResponse.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(createResponse.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> updateResponse = new AtomicReference<>();
        var id = createResponse.get().as(CreatePlayerResponse.class).id();
        Allure.step("Update ADMIN with editor SUPERVISOR", () -> {
            var requestBody = UpdatePlayerRequest.builder()
                    .password(invalidPassword)
                    .build();
            updateResponse.set(steps.updatePlayer(SUPERVISOR.getRole(), id, requestBody));
        });

        Allure.step("Validate status code 400", () -> {
            Assert.assertEquals(updateResponse.get().statusCode(), 400, "status code");
        });
    }
}
