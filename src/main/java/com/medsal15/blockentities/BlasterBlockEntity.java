package com.medsal15.blockentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.handlers.BEStackHandler;
import com.medsal15.blockentities.handlers.FuellessWrapper;
import com.medsal15.blocks.machine.BlasterBlock;
import com.medsal15.config.ConfigServer;
import com.medsal15.menus.BlasterMenu;
import com.medsal15.particles.ESParticleTypes;
import com.mraof.minestuck.api.uranium.IUraniumHandler;
import com.mraof.minestuck.api.uranium.SimpleUraniumHandler;
import com.mraof.minestuck.api.uranium.UraniumPower;
import com.mraof.minestuck.block.machine.MachineBlock;
import com.mraof.minestuck.blockentity.machine.MachineProcessBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class BlasterBlockEntity extends MachineProcessBlockEntity implements MenuProvider {
    public static final String TITLE = "container.extrastuck.blaster";
    public static final int SLOT_FUEL = 0;

    private final IUraniumHandler uraniumHandler = new SimpleUraniumHandler(ConfigServer.BLASTER_URANIUM_STORAGE::get,
            () -> this.fuel, fuel -> this.fuel = fuel) {
        public boolean canExtractUranium() {
            return false;
        }
    };

    private final DataSlot fuelHolder = new DataSlot() {
        public int get() {
            return fuel;
        };

        public void set(int value) {
            fuel = value;
        };
    };

    private int fuel = 0;
    private boolean pulsed = false;
    private boolean particles = false;

    public BlasterBlockEntity(BlockPos pos, BlockState state) {
        super(ESBlockEntities.BLASTER.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, Provider registries) {
        super.loadAdditional(nbt, registries);

        if (nbt.contains("fuel", 2))
            fuel = nbt.getShort("fuel");
        else
            fuel = nbt.getInt("fuel");
        pulsed = nbt.getBoolean("pulsed");
        if (nbt.contains("particles"))
            particles = nbt.getBoolean("particles");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, Provider registries) {
        super.saveAdditional(nbt, registries);

        nbt.putInt("fuel", fuel);
        nbt.putBoolean("pulsed", pulsed);
        if (particles)
            nbt.putBoolean("particles", particles);
    }

    public IItemHandler getItemHandler(@Nullable Direction side) {
        return new FuellessWrapper(itemHandler, SLOT_FUEL, SLOT_FUEL + 1, SLOT_FUEL);
    }

    public IUraniumHandler getUraniumHandler(@Nullable Direction side) {
        return uraniumHandler;
    }

    private static int distInDirection(Level level, BlockPos from, Direction direction, int max) {
        int dist = 0;
        BlockPos next = from;

        for (; dist < max; dist++) {
            next = next.relative(direction);
            BlockState nextState = level.getBlockState(next);
            if (!nextState.isAir()) {
                VoxelShape shape = nextState.getCollisionShape(level, next);
                if (!nextState.canOcclude())
                    continue;
                if (Block.isFaceFull(shape, direction.getOpposite())) {
                    break;
                }
                if (Block.isFaceFull(shape, direction)) {
                    dist++;
                    break;
                }
            }
        }

        return dist;
    }

    private void shoot(int power) {
        if (fuel < power)
            return;

        Level l = level;
        if (l != null) {
            BlockState state = l.getBlockState(worldPosition);
            Direction direction = state.getValue(MachineBlock.FACING);
            int dist = distInDirection(l, worldPosition, direction, power);

            AABB area = new AABB(worldPosition.relative(direction));
            switch (direction) {
                case NORTH:
                    area = area.expandTowards(0, 0, -dist);
                    break;
                case SOUTH:
                    area = area.expandTowards(0, 0, dist);
                    break;
                case EAST:
                    area = area.expandTowards(dist, 0, 0);
                    break;
                case WEST:
                    area = area.expandTowards(-dist, 0, 0);
                    break;
                case UP:
                    area = area.expandTowards(0, dist, 0);
                    break;
                case DOWN:
                    area = area.expandTowards(0, -dist, 0);
                    break;
                default:
                    break;
            }

            l.getEntities((Entity) null, area, entity -> entity instanceof LivingEntity).forEach(entity -> {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 200, 1));
            });

            fuel -= dist;

            if (l instanceof ServerLevel server) {
                BlockPos pos = worldPosition;
                for (int i = 0; i < dist; i++) {
                    pos = pos.relative(direction);
                    server.sendParticles(ESParticleTypes.URANIUM_BLAST.get(), pos.getX() + .5,
                            pos.getY() + .5, pos.getZ() + .5, 1, 0, 0, 0, 0);
                }
            }
            l.playSound(null, worldPosition, SoundEvents.WITHER_SHOOT, SoundSource.BLOCKS, 1F, .5F);
        }
    }

    public boolean canRefuel(ItemStack fuelStack) {
        int amount = UraniumPower.getUraniumPower(fuelStack);
        return fuel + amount <= ConfigServer.CHARGER_URANIUM_STORAGE.get();
    }

    public void addFuel(ItemStack fuelStack) {
        int amount = UraniumPower.getUraniumPower(fuelStack);
        fuel += amount;
    }

    // MachineProcessBlockEntity
    @Override
    protected void tick() {
        ItemStack fuel = itemHandler.getStackInSlot(SLOT_FUEL);
        Level l = level;

        if (canRefuel(fuel)) {
            addFuel(fuel);
            ItemStack taken = itemHandler.extractItem(SLOT_FUEL, 1, false);
            ItemStack remainder = taken.getCraftingRemainingItem();
            if (!remainder.isEmpty() && l != null) {
                ItemEntity remainderEntity = new ItemEntity(l, worldPosition.getX(), worldPosition.getY(),
                        worldPosition.getZ(), remainder);
                l.addFreshEntity(remainderEntity);
            }
        }

        if (l != null) {
            boolean prev = pulsed;
            if (l.hasNeighborSignal(worldPosition)) {
                if (!pulsed && this.fuel > 0) {
                    shoot(l.getBestNeighborSignal(worldPosition));
                    particles = true;
                }
                pulsed = true;
            } else {
                pulsed = false;
            }
            if (prev != pulsed) {
                setChanged();
            }
        }
    }

    // BlockEntity
    @Override
    public void setChanged() {
        Level l = level;
        if (l != null) {
            BlockState state = l.getBlockState(worldPosition);
            if (state.getBlock() instanceof BlasterBlock) {
                state = state.setValue(BlasterBlock.ACTIVE, pulsed);
                l.setBlock(worldPosition, state,
                        Block.UPDATE_ALL_IMMEDIATE | Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_SUPPRESS_DROPS);
            }
        }
        super.setChanged();
    }

    @Override
    protected ItemStackHandler createItemHandler() {
        return new BEStackHandler(1, (slot, stack) -> slot == SLOT_FUEL ? UraniumPower.hasUraniumPower(stack) : false,
                this);
    }

    // MenuProvider
    @Override
    public Component getDisplayName() {
        return Component.translatable(TITLE);
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int containerId, @Nonnull Inventory playerInventory,
            @Nonnull Player player) {
        Level l = level;
        if (l == null)
            return null;

        return new BlasterMenu(containerId, playerInventory, itemHandler, fuelHolder,
                ContainerLevelAccess.create(l, worldPosition), worldPosition);
    }
}
