package com.viditagarwal.running.mixin;

import com.viditagarwal.running.ChronoBootsItem;
import com.viditagarwal.running.PlayerPhaseState;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceGateBlock.class)
public abstract class FenceGateBlockMixin {

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void onGetCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (!(world instanceof World serverWorld) || serverWorld.isClient()) return;

        for (PlayerEntity player : serverWorld.getPlayers()) {
            if (ChronoBootsItem.isWearing(player) && PlayerPhaseState.isPhasing((ServerPlayerEntity) player)) {
                cir.setReturnValue(VoxelShapes.empty());
                return;
            }
        }
    }
}
