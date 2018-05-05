package com.lumberwizard.redstoneigniter;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;

@Config(modid = "redstoneigniter", name = "Redstone Igniter")
public class Configuration {

    @Comment("If set to true, the block will require fire or lava on top of it to function (fire on top of this block will only die if set to false)")
    @RequiresMcRestart
    public static boolean requireFire = true;

}
