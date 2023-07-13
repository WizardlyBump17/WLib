package com.wizardlybump17.wlib.controller.player.reason;

import com.wizardlybump17.wlib.database.controller.player.reason.BasicUnloadReason;
import com.wizardlybump17.wlib.database.controller.player.reason.UnloadReason;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UnloadReasons {

    public static final UnloadReason QUIT = new BasicUnloadReason("QUIT");
    public static final UnloadReason UNKNOWN = new BasicUnloadReason("UNKNOWN");
}
