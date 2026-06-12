package org.clockwork.neogeckojs;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class AnimatablePickaxeItem extends PickaxeItem implements GeoItem {
    private final AnimatableInstanceCache CACHE = GeckoLibUtil.createInstanceCache(this);
    private final Builder builder;

    public AnimatablePickaxeItem(Builder builder) {
        super(builder.toolTier, builder.createItemProperties());
        this.builder = builder;
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel && builder.usingAnimationCallback != null) {
            builder.usingAnimationCallback.call(this, serverLevel, (ServerPlayer) player, hand);
        }
        return super.use(level, player, hand);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (level instanceof ServerLevel serverLevel && builder.finishUsingAnimationCallback != null) {
            builder.finishUsingAnimationCallback.call(this, serverLevel, livingEntity);
        }
        return super.finishUsingItem(itemStack, level, livingEntity);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int tick) {
        if (level instanceof ServerLevel serverLevel && builder.releaseUsingAnimationCallback != null) {
            builder.releaseUsingAnimationCallback.call(this, serverLevel, livingEntity, tick);
        }
        super.releaseUsing(itemStack, level, livingEntity, tick);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private AnimatableItemRenderer<AnimatablePickaxeItem> renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    AnimatableItemRenderer<AnimatablePickaxeItem> itemRenderer = new AnimatableItemRenderer<>(builder.itemModel);
                    if (builder.useEntityGuiLighting) itemRenderer.useAlternateGuiLighting();
                    this.renderer = itemRenderer;
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        builder.controllers.forEach(controller -> registrar.add(controller.build(this)));
        builder.animations.forEach(animation -> registrar.add(new AnimationController<>(this, animation::create)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return CACHE;
    }

    public static class Builder extends AnimatableHandheldItemBuilder<AnimatablePickaxeItem> {
        public Builder(ResourceLocation id) {
            super(id, 1F, -2.8F);
        }

        @Override
        public AnimatablePickaxeItem createObject() {
            return new AnimatablePickaxeItem(this);
        }
    }
}
