package org.clockwork.neogeckojs;

import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.item.custom.ArmorItemBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;

import java.util.ArrayList;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class AnimatableArmorBuilder extends ArmorItemBuilder {
    public final ExtendedGeoModel<AnimatableArmorItem> armorModel = new ExtendedGeoModel<>();
    public final transient ArrayList<AnimationControllerBuilder<AnimatableArmorItem>> controllers = new ArrayList<>();
    public final transient ArrayList<AnimationStateCallback> animations = new ArrayList<>();
    public boolean useGeoModel = false;
    public BoneVisibilityCallback boneVisibilityCallback;

    public AnimatableArmorBuilder tier(Holder<ArmorMaterial> material) {
        return (AnimatableArmorBuilder) material(material);
    }

    public AnimatableArmorBuilder(ResourceLocation id, ArmorItem.Type type) {
        super(id, type);
    }

    public AnimatableArmorBuilder addAnimation(AnimationStateCallback callBack) {
        animations.add(callBack);
        return this;
    }

    public AnimatableArmorBuilder addController(Consumer<AnimationControllerBuilder<AnimatableArmorItem>> consumer) {
        AnimationControllerBuilder<AnimatableArmorItem> builder = new AnimationControllerBuilder<>();
        consumer.accept(builder);
        controllers.add(builder);
        return this;
    }

    public AnimatableArmorBuilder geoModel(Consumer<ExtendedGeoModel.Builder<AnimatableArmorItem>> consumer) {
        consumer.accept(armorModel.builder);
        return this;
    }

    public AnimatableArmorBuilder armorItemUseGeoModel() {
        this.useGeoModel = true;
        return this;
    }

    public AnimatableArmorBuilder defaultGeoModel() {
        armorModel.builder.setSimpleModel(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "geo/armor/" + id.getPath() + ".geo.json"));
        armorModel.builder.setSimpleTexture(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/armor/" + id.getPath() + ".png"));
        armorModel.builder.setSimpleAnimation(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "animations/armor/" + id.getPath() + ".animation.json"));
        return this;
    }

    public AnimatableArmorBuilder boneVisibility(BoneVisibilityCallback callback) {
        this.boneVisibilityCallback = callback;
        return this;
    }

    @Override
    public Item createObject() {
        return new AnimatableArmorItem(this);
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        if (useGeoModel) {
            ResourceLocation parent;
            if (parentModel == null) {
                parent = switch (armorType) {
                    case HELMET -> ResourceLocation.fromNamespaceAndPath("geckojs", "item/helmet");
                    case CHESTPLATE -> ResourceLocation.fromNamespaceAndPath("geckojs", "item/chestplate");
                    case LEGGINGS -> ResourceLocation.fromNamespaceAndPath("geckojs", "item/leggings");
                    case BOOTS -> ResourceLocation.fromNamespaceAndPath("geckojs", "item/boots");
                    default -> ResourceLocation.fromNamespaceAndPath("geckojs", "item/item");
                };
            } else {
                parent = parentModel;
            }
            generator.itemModel(id, model -> model.parent(parent));
        } else {
            super.generateAssets(generator);
        }
    }

    @FunctionalInterface
    public interface AnimationStateCallback {
        PlayState create(AnimationState<AnimatableArmorItem> state);
    }

    @FunctionalInterface
    public interface BoneVisibilityCallback {
        void apply(IAnimatableArmorRenderer renderer, EquipmentSlot slot);
    }
}
