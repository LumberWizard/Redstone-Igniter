package com.lumberwizard.redstoneigniter;

import com.lumberwizard.redstoneigniter.compat.WailaInfoProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Random;

public class BlockIgniter extends Block implements WailaInfoProvider {

    private Random random = new Random();

    public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockIgniter() {
        super(Material.ROCK);
        setUnlocalizedName(ModRedstoneIgniter.MODID + ".igniter");
        setRegistryName("igniter");
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TRIGGERED, false));
        setSoundType(SoundType.STONE);
        setHardness(0.5F);
        setCreativeTab(CreativeTabs.REDSTONE);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModRedstoneIgniter.logger.log(Level.INFO, "Initiating igniter model");
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, TRIGGERED);
    }


    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(TRIGGERED, false), 2);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7)).withProperty(TRIGGERED, meta > 7);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING).getIndex()) | (state.getValue(TRIGGERED) ? 8 : 0);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(TRIGGERED, false);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        boolean flag = worldIn.isBlockPowered(pos);
        boolean flag1 = state.getValue(TRIGGERED);

        if (flag && !flag1)
        {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
            ignite(worldIn, pos, state);
        }
        else if (!flag && flag1)
        {
            worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state){
        return new TileIgniter();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP && Configuration.requireFire;
    }

    private BlockPos getFirePos(BlockPos pos, IBlockState state){
        switch (state.getValue(FACING)){
            case EAST:
                return pos.east();
            case WEST:
                return pos.west();
            case SOUTH:
                return pos.south();
            case DOWN:
                return pos.down();
            case UP:
                return pos.up();
            default:
                return pos.north();
        }
    }



    private void ignite(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote) {
            return;
        }
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity == null || !(tileEntity instanceof TileIgniter) || tileEntity.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(100, true) < 16 || (Configuration.requireFire && !(world.getBlockState(pos.up()).getBlock().equals(Blocks.FIRE) || world.getBlockState(pos.up()).getBlock().equals(Blocks.LAVA) || world.getBlockState(pos.up()).getBlock().equals(Blocks.FLOWING_LAVA)))) {
            return;
        }
        BlockPos newPos = getFirePos(pos, state);
        if (world.isAirBlock(newPos) && world.isSideSolid(newPos.down(), EnumFacing.UP)) {
            world.setBlockState(newPos, Blocks.FIRE.getDefaultState(), 11);
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F,random.nextFloat() * 0.4F + 0.8F, false);
            ((TileIgniter) tileEntity).getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(100, false);
        }
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntity te = accessor.getTileEntity();
        if (te instanceof TileIgniter) {
            TileIgniter igniterTile = (TileIgniter) te;
            currenttip.add("Energy: " + igniterTile.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored() + "/1000 FE.");
        }
        return currenttip;
    }

}
