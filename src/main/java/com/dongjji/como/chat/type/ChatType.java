package com.dongjji.como.chat.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatType {
    UNKNOWN("(알 수 없는 이름)");

    private final String name;
}
