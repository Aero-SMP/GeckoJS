package org.clockwork.neogeckojs;

import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;

import java.util.ArrayList;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class AnimatableBlockEntityInfo extends BlockEntityInfo {
    public final transient ArrayList<AnimationStateCallback> animations = new ArrayList<>();
    public final transient ArrayList<AnimationControllerBuilder<AnimatableBlockEntity>> controllers = new ArrayList<>();

    public AnimatableBlockEntityInfo(AnimatableBlockBuilder blockBuilder) {
        super(blockBuilder);
    }

    public AnimatableBlockEntityInfo addAnimation(AnimationStateCallback callBack) {
        animations.add(callBack);
        return this;
    }

    public AnimatableBlockEntityInfo addController(Consumer<AnimationControllerBuilder<AnimatableBlockEntity>> consumer) {
        AnimationControllerBuilder<AnimatableBlockEntity> builder = new AnimationControllerBuilder<>();
        consumer.accept(builder);
        controllers.add(builder);
        return this;
    }

    @Override
    public AnimatableBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AnimatableBlockEntity(pos, state, this);
    }

    @FunctionalInterface
    public interface AnimationStateCallback {
        PlayState create(AnimationState<AnimatableBlockEntity> state);
    }
}
