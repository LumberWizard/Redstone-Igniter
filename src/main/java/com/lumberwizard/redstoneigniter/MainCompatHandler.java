package com.lumberwizard.redstoneigniter;

import com.lumberwizard.redstoneigniter.compat.WailaCompatibility;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Level;

public class MainCompatHandler {

    public static void registerWaila() {
        ModRedstoneIgniter.logger.log(Level.INFO, Loader.isModLoaded("waila"));
        if (Loader.isModLoaded("waila")) {
            WailaCompatibility.register();
        }
    }

}
