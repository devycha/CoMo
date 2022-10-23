package com.dongjji.como.user.auth.provider;

import java.time.LocalDate;
import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    public int birthYear() {
        return Integer.parseInt((String) attributes.get("birthyear"));
    }

    public int birthMonth() {
        return Integer.parseInt(((String) attributes.get("birthday")).split("-")[0]);
    }

    public int birthDate() {
        return Integer.parseInt(((String) attributes.get("birthday")).split("-")[1]);
    }

    public LocalDate birth() {
        return LocalDate.of(birthYear(), birthMonth(), birthDate());
    }
}
