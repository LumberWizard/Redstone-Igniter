package com.lumberwizard.redstoneigniter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class TileIgniter extends TileEntity implements ICapabilityProvider {

    private EnergyStorage energyStorage;
    private EnumFacing facing;

    public TileIgniter() {
        this(EnumFacing.NORTH);
    }

    public TileIgniter(EnumFacing facing) {
        super();
        energyStorage = new EnergyStorage(1000);
        this.facing = facing;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if(capability == CapabilityEnergy.ENERGY && facing != this.facing && (!Configuration.requireFire || facing != EnumFacing.UP))
        {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if(capability == CapabilityEnergy.ENERGY && facing != this.facing && (!Configuration.requireFire || facing != EnumFacing.UP))
        {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    //Read and Write Energy from/to NBT (on world load/world unload --> persistent data)
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if(energyStorage != null)
        {
            compound.setTag("ENERGY", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));
        }
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (energyStorage != null && compound.hasKey("ENERGY")){
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, compound.getTag("ENERGY"));
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

}
