package com.lumberwizard.redstoneigniter;

import com.lumberwizard.redstoneigniter.proxy.CommonProxy;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ModRedstoneIgniter.MODID, name = ModRedstoneIgniter.NAME, version = ModRedstoneIgniter.VERSION)
public class ModRedstoneIgniter
{
    public static final String MODID = "redstoneigniter";
    public static final String NAME = "Redstone Igniter";
    public static final String VERSION = "0.1";

    @SidedProxy(clientSide = "com.lumberwizard.redstoneigniter.proxy.ClientProxy", serverSide = "com.lumberwizard.redstoneigniter.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static ModRedstoneIgniter instance;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }
}
