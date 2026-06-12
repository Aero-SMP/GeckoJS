package org.clockwork.neogeckojs;

import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
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
public abstract class AbstractAnimatableItemBuilder<T extends Item & GeoItem> extends ItemBuilder {
    public final ExtendedGeoModel<T> itemModel = new ExtendedGeoModel<>();
    public UsingAnimationCallback<T> usingAnimationCallback;
    public FinishUsingAnimationCallback<T> finishUsingAnimationCallback;
    public ReleaseUsingAnimationCallback<T> releaseUsingAnimationCallback;
    public final transient ArrayList<AnimationControllerBuilder<T>> controllers = new ArrayList<>();
    public final transient ArrayList<AnimationStateCallback<T>> animations = new ArrayList<>();
    public transient boolean useEntityGuiLighting = false;

    public AbstractAnimatableItemBuilder(ResourceLocation id) {
        super(id);
    }

    public AbstractAnimatableItemBuilder<T> usingAnimation(UsingAnimationCallback<T> callback) {
        this.usingAnimationCallback = callback;
        return this;
    }

    public AbstractAnimatableItemBuilder<T> finishUsingAnimation(FinishUsingAnimationCallback<T> callback) {
        this.finishUsingAnimationCallback = callback;
        return this;
    }

    public AbstractAnimatableItemBuilder<T> releaseUsingAnimation(ReleaseUsingAnimationCallback<T> callback) {
        this.releaseUsingAnimationCallback = callback;
        return this;
    }

    public AbstractAnimatableItemBuilder<T> addAnimation(AnimationStateCallback<T> callBack) {
        animations.add(callBack);
        return this;
    }

    public AbstractAnimatableItemBuilder<T> addController(Consumer<AnimationControllerBuilder<T>> consumer) {
        AnimationControllerBuilder<T> builder = new AnimationControllerBuilder<>();
        consumer.accept(builder);
        controllers.add(builder);
        return this;
    }

    public AbstractAnimatableItemBuilder<T> geoModel(Consumer<ExtendedGeoModel.Builder<T>> consumer) {
        consumer.accept(itemModel.builder);
        return this;
    }

    public AbstractAnimatableItemBuilder<T> defaultGeoModel() {
        itemModel.builder.setSimpleModel(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "geo/item/" + id.getPath() + ".geo.json"));
        itemModel.builder.setSimpleTexture(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/item/" + id.getPath() + ".png"));
        itemModel.builder.setSimpleAnimation(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "animations/item/" + id.getPath() + ".animation.json"));
        return this;
    }

    public AbstractAnimatableItemBuilder<T> useEntityGuiLighting() {
        this.useEntityGuiLighting = true;
        return this;
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        generator.itemModel(id, model -> model.parent(parentModel == null ? ResourceLocation.fromNamespaceAndPath("geckojs", "item/item") : parentModel));
    }

    @FunctionalInterface
    public interface UsingAnimationCallback<T extends Item & GeoItem> {
        void call(T self, ServerLevel serverLevel, ServerPlayer player, InteractionHand hand);
    }

    @FunctionalInterface
    public interface FinishUsingAnimationCallback<T extends Item & GeoItem> {
        void call(T self, ServerLevel serverLevel, LivingEntity livingEntity);
    }

    @FunctionalInterface
    public interface ReleaseUsingAnimationCallback<T extends Item & GeoItem> {
        void call(T self, ServerLevel serverLevel, LivingEntity livingEntity, int tick);
    }

    @FunctionalInterface
    public interface AnimationStateCallback<T extends Item & GeoItem> {
        PlayState create(AnimationState<T> state);
    }
}
