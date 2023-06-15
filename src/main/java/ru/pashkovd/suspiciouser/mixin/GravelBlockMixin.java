package ru.pashkovd.suspiciouser.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GravelBlock.class)
public abstract class GravelBlockMixin extends FallingBlock {

    public GravelBlockMixin(Properties p_53205_) {
        super(p_53205_);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (player.getOffhandItem().getItem().equals(Items.BRUSH)) {
            world.setBlockAndUpdate(pos, Blocks.SUSPICIOUS_GRAVEL.defaultBlockState());
            BrushableBlockEntity be = (BrushableBlockEntity) world.getBlockEntity(pos);
            CompoundTag nbt = new CompoundTag();
            nbt.put("item", player.getMainHandItem().serializeNBT());
            assert be != null;
            be.deserializeNBT(nbt);
            world.setBlockEntity(be);
            player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
