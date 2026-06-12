package org.clockwork.neogeckojs;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class AnimatableArmorItem extends ArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final AnimatableArmorBuilder builder;

    public AnimatableArmorItem(AnimatableArmorBuilder builder) {
        super(builder.material, builder.armorType, builder.createItemProperties());
        this.builder = builder;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private AnimatableArmorRenderer<AnimatableArmorItem> armorRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (armorRenderer == null) {
                    this.armorRenderer = new AnimatableArmorRenderer<>(builder.armorModel, builder.boneVisibilityCallback);
                }
                armorRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return armorRenderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        builder.controllers.forEach(controller -> controllers.add(controller.build(this)));
        builder.animations.forEach(animation -> controllers.add(new AnimationController<>(this, animation::create)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}