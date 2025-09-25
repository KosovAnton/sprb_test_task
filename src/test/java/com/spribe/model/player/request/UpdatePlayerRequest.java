package com.spribe.model.player.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdatePlayerRequest(
        Integer age,
        String gender,
        String login,
        String password,
        String role,
        String screenName
) {
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Integer age;
        private String gender;
        private String login;
        private String password;
        private String role;
        private String screenName;

        private Builder() {
        }

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder screenName(String screenName) {
            this.screenName = screenName;
            return this;
        }

        public UpdatePlayerRequest build() {
            return new UpdatePlayerRequest(age, gender, login, password, role, screenName);
        }
    }
}
