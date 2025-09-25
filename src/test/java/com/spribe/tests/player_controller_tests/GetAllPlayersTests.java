package com.spribe.tests.player_controller_tests;

import com.spribe.model.player.response.GetAllPlayersResponse;
import com.spribe.steps.PlayerSteps;
import com.spribe.tests.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Player controller")
@Feature("Get all players")
public class GetAllPlayersTests extends BaseTest {
    private final PlayerSteps steps = new PlayerSteps();

    @Test(description = "Validate status code and content type")
    @Description("""
            **Validate:**
            - status code - 200
            - `Content-Type: application/json`
            """)
    public void validateGetAllPlayersStatusCodeAndContentType() {
        var response = steps.getAllPlayers();
        var statusCode = response.statusCode();
        var headers = response.getHeaders();
        Assert.assertEquals(statusCode, 200, "status code");
        Assert.assertTrue(headers.hasHeaderWithName("Content-Type"), "response contains header");
        Assert.assertEquals(headers.get("Content-Type").getValue(), "application/json", "content type value");
    }

    @Test(description = "Validate player with screenName testSupervisor")
    @Description("""
            **Validate:**
            - list of players is not empty
            - player with screenName testSupervisor
            """)
    public void validateSupervisorPlayer() {
        var response = steps.getAllPlayers();
        var players = response.as(GetAllPlayersResponse.class).players();
        Assert.assertFalse(players.isEmpty(), "players list is not empty");
        var supervisorUserExist = players.stream().anyMatch(player ->
                player.screenName().equals("testSupervisor")
                        && player.age().equals(28)
                        && player.gender().equals("male"));
        Assert.assertTrue(supervisorUserExist, "supervisor player present");
    }
}
