package com.learn.roomseervice;

import com.learn.roomseervice.dto.UserRoomInfo;

public class StrategyFactory {

    public static Strategy getStrategy(UserRoomInfo user) {
        if ("Atharva".equalsIgnoreCase(user.getUsername())) {
            return new AtharvaStrategy();
        }
        return new DefaultStrategy();
    }
}