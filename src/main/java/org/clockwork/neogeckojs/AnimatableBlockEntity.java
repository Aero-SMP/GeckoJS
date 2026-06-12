package org.clockwork.neogeckojs;

import dev.latvian.mods.kubejs.block.entity.KubeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;

public class AnimatableBlockEntity extends KubeBlockEntity implements GeoBlockEntity {
    private final AnimatableBlockEntityInfo entityInfo;
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);

    public AnimatableBlockEntity(BlockPos blockPos, BlockState blockState, AnimatableBlockEntityInfo entityInfo) {
        super(blockPos, blockState, entityInfo);
        this.entityInfo = entityInfo;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        entityInfo.controllers.forEach(controller -> controllers.add(controller.build(this)));
        entityInfo.animations.forEach(animation -> controllers.add(new AnimationController<>(this, animation::create)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }
}
