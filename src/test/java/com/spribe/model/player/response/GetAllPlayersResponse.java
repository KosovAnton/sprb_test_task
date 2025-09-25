package com.spribe.model.player.response;

import java.util.List;

public record GetAllPlayersResponse(List<Player> players) {
    public record Player(
            Integer id,
            String screenName,
            String gender,
            Integer age
    ) {
    }
}
