package org.clockwork.neogeckojs;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.animatable.GeoItem;

public class AnimatableArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> implements IAnimatableArmorRenderer {
    private final AnimatableArmorBuilder.BoneVisibilityCallback boneVisibilityCallback;

    public AnimatableArmorRenderer(ExtendedGeoModel<T> model, AnimatableArmorBuilder.BoneVisibilityCallback boneVisibilityCallback) {
        super(model);
        this.scaleWidth = model.builder.scaleWidth;
        this.scaleHeight = model.builder.scaleHeight;
        this.boneVisibilityCallback = boneVisibilityCallback;
        if (model.builder.autoGlowing) {
            addRenderLayer(new AutoGlowingGeoLayer<>(this));
        }
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        if (boneVisibilityCallback == null) {
            super.applyBoneVisibilityBySlot(currentSlot);
        } else {
            boneVisibilityCallback.apply(this, currentSlot);
        }
    }

    @Override
    public void setBoneVisible(@Nullable GeoBone bone, boolean visible) {
        super.setBoneVisible(bone, visible);
    }

    @Override
    public GeoBone getHeadBone() {
        return this.head;
    }

    @Override
    public GeoBone getBodyBone() {
        return this.body;
    }

    @Override
    public GeoBone getRightArmBone() {
        return this.rightArm;
    }

    @Override
    public GeoBone getLeftArmBone() {
        return this.leftArm;
    }

    @Override
    public GeoBone getRightLegBone() {
        return this.rightLeg;
    }

    @Override
    public GeoBone getLeftLegBone() {
        return this.leftLeg;
    }

    @Override
    public GeoBone getRightBootBone() {
        return this.rightBoot;
    }

    @Override
    public GeoBone getLeftBootBone() {
        return this.leftBoot;
    }

    @Override
    public @Nullable GeoBone getBone(String name) {
        return this.model.getBone(name).orElse(null);
    }
}
