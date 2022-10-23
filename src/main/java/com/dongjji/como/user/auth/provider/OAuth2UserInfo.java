package com.dongjji.como.user.auth.provider;

public interface OAuth2UserInfo {
    String getName();
    String getProvider();
    String getProviderId();
    String getEmail();
}