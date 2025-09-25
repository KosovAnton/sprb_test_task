package com.spribe.model.player.response;

public record CreatePlayerResponse(
        Integer id,
        String login,
        String password,
        String screenName,
        String gender,
        Integer age,
        String role
) {
}
