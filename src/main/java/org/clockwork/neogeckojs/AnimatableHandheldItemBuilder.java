package org.clockwork.neogeckojs;

import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;

import java.util.ArrayList;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class AnimatableHandheldItemBuilder<T extends Item & GeoItem> extends HandheldItemBuilder {
    public final ExtendedGeoModel<T> itemModel = new ExtendedGeoModel<>();
    public AbstractAnimatableItemBuilder.UsingAnimationCallback<T> usingAnimationCallback;
    public AbstractAnimatableItemBuilder.FinishUsingAnimationCallback<T> finishUsingAnimationCallback;
    public AbstractAnimatableItemBuilder.ReleaseUsingAnimationCallback<T> releaseUsingAnimationCallback;
    public final transient ArrayList<AnimationControllerBuilder<T>> controllers = new ArrayList<>();
    public final transient ArrayList<AbstractAnimatableItemBuilder.AnimationStateCallback<T>> animations = new ArrayList<>();
    public transient boolean useEntityGuiLighting = false;

    public AnimatableHandheldItemBuilder(ResourceLocation id, float attackDamage, float attackSpeed) {
        super(id, attackDamage, attackSpeed);
    }

    public AnimatableHandheldItemBuilder<T> usingAnimation(AbstractAnimatableItemBuilder.UsingAnimationCallback<T> callback) {
        this.usingAnimationCallback = callback;
        return this;
    }

    public AnimatableHandheldItemBuilder<T> finishUsingAnimation(AbstractAnimatableItemBuilder.FinishUsingAnimationCallback<T> callback) {
        this.finishUsingAnimationCallback = callback;
        return this;
    }

    public AnimatableHandheldItemBuilder<T> releaseUsingAnimation(AbstractAnimatableItemBuilder.ReleaseUsingAnimationCallback<T> callback) {
        this.releaseUsingAnimationCallback = callback;
        return this;
    }

    public AnimatableHandheldItemBuilder<T> addAnimation(AbstractAnimatableItemBuilder.AnimationStateCallback<T> callBack) {
        animations.add(callBack);
        return this;
    }

    public AnimatableHandheldItemBuilder<T> addController(Consumer<AnimationControllerBuilder<T>> consumer) {
        AnimationControllerBuilder<T> builder = new AnimationControllerBuilder<>();
        consumer.accept(builder);
        controllers.add(builder);
        return this;
    }

    public AnimatableHandheldItemBuilder<T> geoModel(Consumer<ExtendedGeoModel.Builder<T>> consumer) {
        consumer.accept(itemModel.builder);
        return this;
    }

    public AnimatableHandheldItemBuilder<T> defaultGeoModel() {
        itemModel.builder.setSimpleModel(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "geo/item/" + id.getPath() + ".geo.json"));
        itemModel.builder.setSimpleTexture(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/item/" + id.getPath() + ".png"));
        itemModel.builder.setSimpleAnimation(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "animations/item/" + id.getPath() + ".animation.json"));
        return this;
    }

    public AnimatableHandheldItemBuilder<T> useEntityGuiLighting() {
        this.useEntityGuiLighting = true;
        return this;
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        generator.itemModel(id, model -> model.parent(parentModel == null ? ResourceLocation.fromNamespaceAndPath("geckojs", "item/item") : parentModel));
    }
}
