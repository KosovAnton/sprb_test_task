package com.spribe.tests.player_controller_tests;

import com.spribe.dataproviders.PasswordDataProvider;
import com.spribe.helpers.StringGeneratorHelper;
import com.spribe.model.player.response.CreatePlayerResponse;
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
@Feature("Create player")
public class CreatePlayerTests extends BaseTest {
    private final PlayerSteps steps = new PlayerSteps();

    @Test(description = "Validate that ADMIN can be created with editor - SUPERVISOR")
    @Description("""
            **Validate:**
            - ADMIN can be created with editor - SUPERVISOR
            - status code 200
            - validate response body
            """)
    public void validateAdminCreatedWithSupervisorEditor() {
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

        Allure.step("Validate response body", () -> {
            SoftAssert softAssert = new SoftAssert();
            var responseBody = response.get().as(CreatePlayerResponse.class);
            softAssert.assertNotNull(responseBody.id(), "id");
            softAssert.assertEquals(responseBody.login(), login, "login");
            softAssert.assertEquals(responseBody.password(), password, "password");
            softAssert.assertEquals(responseBody.screenName(), screenName, "screenName");
            softAssert.assertEquals(responseBody.gender(), gender, "gender");
            softAssert.assertEquals(responseBody.age(), age, "age");
            softAssert.assertEquals(responseBody.role(), role, "role");
            softAssert.assertAll();
        });
    }

    @Test(description = "Validate that ADMIN can be created with editor - ADMIN")
    @Description("""
            **Validate:**
            - ADMIN can be created with editor - ADMIN
            - status code 200
            """)
    public void validateAdminCreatedWithAdminEditor() {
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

        AtomicReference<Response> response2 = new AtomicReference<>();
        Allure.step("Create ADMIN with editor ADMIN", () -> {
            var editor = response.get().as(CreatePlayerResponse.class).login();
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();

            response2.set(steps.createPlayer(
                    editor,
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    ADMIN.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(response2.get().statusCode(), 200, "status code");
        });
    }

    @Test(description = "Validate that USER can be created with editor - ADMIN")
    @Description("""
            **Validate:**
            - USER can be created with editor - ADMIN
            - status code 200
            """)
    public void validateUserCreatedWithAdminEditor() {
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

        AtomicReference<Response> response2 = new AtomicReference<>();
        Allure.step("Create USER with editor ADMIN", () -> {
            var editor = response.get().as(CreatePlayerResponse.class).login();
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();

            response2.set(steps.createPlayer(
                    editor,
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    USER.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(response2.get().statusCode(), 200, "status code");
        });
    }

    @Test(description = "Validate that USER cannot be created with editor - USER")
    @Description("""
            **Validate:**
            - USER cannot be created with editor - USER
            - status code 403 - Forbidden
            """)
    public void validateUserCreatedWithEditorUser() {
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
                    USER.getRole(), screenName));
        });

        Allure.step("Validate status code 200", () -> {
            Assert.assertEquals(response.get().statusCode(), 200, "status code");
        });

        AtomicReference<Response> response2 = new AtomicReference<>();
        Allure.step("Create USER with editor USER", () -> {
            var editor = response.get().as(CreatePlayerResponse.class).login();
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();

            response2.set(steps.createPlayer(
                    editor,
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    USER.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 403", () -> {
            Assert.assertEquals(response2.get().statusCode(), 403, "status code");
        });
    }

    @Test(description = "Validate that SUPERVISOR cannot be created with editor - ADMIN")
    @Description("""
            **Validate:**
            - SUPERVISOR cannot be created with editor - ADMIN
            - status code 403 - Forbidden
            """)
    public void validateSupervisorCannotBeCreatedWithEditorAdmin() {
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

        AtomicReference<Response> response2 = new AtomicReference<>();
        Allure.step("Create SUPERVISOR with editor ADMIN", () -> {
            var editor = response.get().as(CreatePlayerResponse.class).login();
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();

            response2.set(steps.createPlayer(
                    editor,
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    SUPERVISOR.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 403", () -> {
            Assert.assertEquals(response2.get().statusCode(), 403, "status code");
        });
    }

    @Test(description = "Validate that ADMIN cannot be created with editor - USER")
    @Description("""
            **Validate:**
            - ADMIN cannot be created with editor - USER
            - status code 403 - Forbidden
            """)
    public void validateAdminCannotBeCreatedWithEditorUser() {
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

        AtomicReference<Response> response2 = new AtomicReference<>();
        Allure.step("Create ADMIN with editor USER", () -> {
            var editor = response.get().as(CreatePlayerResponse.class).login();
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();

            response2.set(steps.createPlayer(
                    editor,
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    ADMIN.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 403", () -> {
            Assert.assertEquals(response2.get().statusCode(), 403, "status code");
        });
    }

    @Test(description = "Validate that SUPERVISOR cannot be created with editor - SUPERVISOR")
    @Description("""
            **Validate:**
            - SUPERVISOR cannot be created with editor - SUPERVISOR
            - status code 200
            """)
    public void validateSupervisorCreatedWithEditorSupervisor() {
        AtomicReference<Response> response = new AtomicReference<>();
        Allure.step("Create SUPERVISOR with editor SUPERVISOR", () -> {
            var login = StringGeneratorHelper.generatePlayerLogin();
            var password = StringGeneratorHelper.generatePassword();
            var screenName = StringGeneratorHelper.generateScreenName();
            response.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    SUPERVISOR.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 403", () -> {
            Assert.assertEquals(response.get().statusCode(), 403, "status code");
        });
    }

    @Test(description = "Validate that ADMIN cannot be created with editor - SUPERVISOR, when age 16")
    @Description("""
            **Validate:**
            - ADMIN cannot be created with editor - SUPERVISOR, when age 16
            - status code 400 - Bad Request
            """)
    public void validateAdminNotCreatedWithSupervisorEditorAndAge16() {
        AtomicReference<Response> response = new AtomicReference<>();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword();
        var screenName = StringGeneratorHelper.generateScreenName();
        var age = 16;
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

        Allure.step("Validate status code 400", () -> {
            Assert.assertEquals(response.get().statusCode(), 400, "status code");
        });
    }

    @Test(description = "Validate that USER cannot be created with editor - SUPERVISOR, when age 60")
    @Description("""
            **Validate:**
            - USER cannot be created with editor - SUPERVISOR, when age 60
            - status code 400 - Bad Request
            """)
    public void validateUserNotCreatedWithSupervisorEditorAndAge60() {
        AtomicReference<Response> response = new AtomicReference<>();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword();
        var screenName = StringGeneratorHelper.generateScreenName();
        var age = 60;
        var gender = MALE.getGender();
        var role = USER.getRole();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            response.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 400", () -> {
            Assert.assertEquals(response.get().statusCode(), 400, "status code");
        });
    }

    @Test(description = "Validate that USER cannot be created with editor - SUPERVISOR, when password length - 6 characters")
    @Description("""
            **Validate:**
            - USER cannot be created with editor - SUPERVISOR, when password length - 6 characters
            - status code 400 - Bad Request
            """)
    public void validateUserNotCreatedWithSupervisorEditorPass6Chars() {
        AtomicReference<Response> response = new AtomicReference<>();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword(6);
        var screenName = StringGeneratorHelper.generateScreenName();
        var age = 17;
        var gender = MALE.getGender();
        var role = USER.getRole();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            response.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 400", () -> {
            Assert.assertEquals(response.get().statusCode(), 400, "status code");
        });
    }

    @Test(description = "Validate that USER can be created with editor - SUPERVISOR, when password length - 7 characters")
    @Description("""
            **Validate:**
            - USER can be created with editor - SUPERVISOR, when password length - 7 characters
            - status code 200
            """)
    public void validateUserCreatedWithSupervisorEditorPass7Chars() {
        AtomicReference<Response> response = new AtomicReference<>();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword(7);
        var screenName = StringGeneratorHelper.generateScreenName();
        var age = 17;
        var gender = MALE.getGender();
        var role = USER.getRole();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
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
    }

    @Test(description = "Validate that USER cannot be created with editor - SUPERVISOR, when password length - 16 characters")
    @Description("""
            **Validate:**
            - USER cannot be created with editor - SUPERVISOR, when password length - 16 characters
            - status code 400 - Bad Request
            """)
    public void validateUserNotCreatedWithSupervisorEditorPass16Chars() {
        AtomicReference<Response> response = new AtomicReference<>();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword(16);
        var screenName = StringGeneratorHelper.generateScreenName();
        var age = 17;
        var gender = MALE.getGender();
        var role = USER.getRole();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            response.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    password,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 400", () -> {
            Assert.assertEquals(response.get().statusCode(), 400, "status code");
        });
    }

    @Test(description = "Validate that USER can be created with editor - SUPERVISOR, when password length - 15 characters")
    @Description("""
            **Validate:**
            - USER can be created with editor - SUPERVISOR, when password length - 15 characters
            - status code 200
            """)
    public void validateUserCreatedWithSupervisorEditorPass15Chars() {
        AtomicReference<Response> response = new AtomicReference<>();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword(15);
        var screenName = StringGeneratorHelper.generateScreenName();
        var age = 17;
        var gender = MALE.getGender();
        var role = USER.getRole();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
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
    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = PasswordDataProvider.class,
            description = "Validate that USER cannot be created with editor - SUPERVISOR, when password invalid")
    @Description("""
            **Validate:**
            - USER cannot be created with editor - SUPERVISOR, when password invalid
            - status code 400 - Bad Request
            """)
    public void validateUserNotCreatedWithSupervisorEditorWithInvalidPassword(String invalidPassword) {
        AtomicReference<Response> response = new AtomicReference<>();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var screenName = StringGeneratorHelper.generateScreenName();
        var age = 17;
        var gender = MALE.getGender();
        var role = USER.getRole();
        Allure.step("Create USER with editor SUPERVISOR", () -> {
            response.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    age,
                    gender,
                    login,
                    invalidPassword,
                    role,
                    screenName));
        });

        Allure.step("Validate status code 400", () -> {
            Assert.assertEquals(response.get().statusCode(), 400, "status code");
        });
    }

    @Test(description = "Validate that ADMIN can be created with editor - SUPERVISOR, with gender - FEMALE")
    @Description("""
            **Validate:**
            - ADMIN can be created with editor - SUPERVISOR, with gender - FEMALE
            - status code 200
            """)
    public void validateAdminCreatedWithSupervisorEditorGenderFemale() {
        AtomicReference<Response> response = new AtomicReference<>();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword();
        var screenName = StringGeneratorHelper.generateScreenName();
        Integer age = 17;
        var gender = FEMALE.getGender();
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
    }

    @Test(description = "Validate that ADMIN can be created with editor - SUPERVISOR, with gender - wrong value")
    @Description("""
            **Validate:**
            - ADMIN can be created with editor - SUPERVISOR, with gender - wrong value
            - status code 400 - Bad Request
            """)
    public void validateAdminCreatedWithSupervisorEditorGenderWrongValue() {
        AtomicReference<Response> response = new AtomicReference<>();
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword();
        var screenName = StringGeneratorHelper.generateScreenName();
        var age = 17;
        var gender = "mail";
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

        Allure.step("Validate status code 400", () -> {
            Assert.assertEquals(response.get().statusCode(), 400, "status code");
        });
    }

    @Test(description = "Validate existing player cannot be updated with GET /player/create/{editor}")
    @Description("""
            Test creates one player and using same login, password, screenName creates another one
            **Validate:**
            - existing player cannot be updated with GET /player/create/{editor}
            - status code 409 Conflict 
            """)
    public void validateExistingPlayerCannotBeUpdated() {
        var login = StringGeneratorHelper.generatePlayerLogin();
        var password = StringGeneratorHelper.generatePassword();
        var screenName = StringGeneratorHelper.generateScreenName();

        AtomicReference<Response> response = new AtomicReference<>();
        Allure.step("Create ADMIN with editor SUPERVISOR", () -> {
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

        AtomicReference<Response> response2 = new AtomicReference<>();
        Allure.step("Create ADMIN with editor ADMIN", () -> {
            response2.set(steps.createPlayer(
                    SUPERVISOR.getRole(),
                    17,
                    MALE.getGender(),
                    login,
                    password,
                    ADMIN.getRole(),
                    screenName));
        });

        Allure.step("Validate status code 409", () -> {
            Assert.assertEquals(response2.get().statusCode(), 409, "status code");
        });
    }
}
