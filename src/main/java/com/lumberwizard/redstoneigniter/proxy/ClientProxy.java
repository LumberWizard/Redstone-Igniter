package com.lumberwizard.redstoneigniter.proxy;

import com.lumberwizard.redstoneigniter.ModBlocks;
import com.lumberwizard.redstoneigniter.ModRedstoneIgniter;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Level;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModRedstoneIgniter.logger.log(Level.INFO, "Registering models");
        ModBlocks.initModels();
        ModelLoader.setCustomStateMapper(ModBlocks.igniter, new StateMap.Builder().ignore(ModBlocks.igniter.TRIGGERED).build());
    }
}