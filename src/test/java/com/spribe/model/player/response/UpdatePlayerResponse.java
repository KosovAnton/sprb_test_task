package com.spribe.model.player.response;

public record UpdatePlayerResponse(
        Integer age,
        String gender,
        Integer id,
        String login,
        String role,
        String screenName
) {
}
