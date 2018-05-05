package com.lumberwizard.redstoneigniter;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

public class ModBlocks {
    @GameRegistry.ObjectHolder("redstoneigniter:igniter")
    public static BlockIgniter igniter;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        ModRedstoneIgniter.logger.log(Level.INFO, "Initiating block models");
        igniter.initModel();
    }
}
