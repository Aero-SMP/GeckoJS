package org.clockwork.neogeckojs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.animation.EasingType;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;

public class GeckoJSPlugin implements KubeJSPlugin {

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {
        registry.of(Registries.BLOCK, callback -> {
            callback.add("animatable", AnimatableBlockBuilder.class, AnimatableBlockBuilder::new);
        });
        registry.of(Registries.ITEM, callback -> {
            callback.add("animatable", AnimatableItem.Builder.class, AnimatableItem.Builder::new);
            callback.add("anim_helmet", AnimatableArmorBuilder.class, id -> new AnimatableArmorBuilder(id, ArmorItem.Type.HELMET));
            callback.add("anim_chestplate", AnimatableArmorBuilder.class, id -> new AnimatableArmorBuilder(id, ArmorItem.Type.CHESTPLATE));
            callback.add("anim_leggings", AnimatableArmorBuilder.class, id -> new AnimatableArmorBuilder(id, ArmorItem.Type.LEGGINGS));
            callback.add("anim_boots", AnimatableArmorBuilder.class, id -> new AnimatableArmorBuilder(id, ArmorItem.Type.BOOTS));
            callback.add("anim_shield", AnimatableShieldItem.Builder.class, AnimatableShieldItem.Builder::new);
            callback.add("anim_sword", AnimatableSwordItem.Builder.class, AnimatableSwordItem.Builder::new);
            callback.add("anim_pickaxe", AnimatablePickaxeItem.Builder.class, AnimatablePickaxeItem.Builder::new);
            callback.add("anim_axe", AnimatableAxeItem.Builder.class, AnimatableAxeItem.Builder::new);
            callback.add("anim_shovel", AnimatableShovelItem.Builder.class, AnimatableShovelItem.Builder::new);
            callback.add("anim_hoe", AnimatableHoeItem.Builder.class, AnimatableHoeItem.Builder::new);
        });
    }

    @Override
    public void registerBindings(BindingRegistry registry) {
        registry.add("RawAnimation", RawAnimation.class);
        registry.add("GeoItem", GeoItem.class);
        registry.add("EasingType", EasingType.class);
        registry.add("PlayState", PlayState.class);
        registry.add("DataTickets", DataTickets.class);
        registry.add("EquipmentSlot", EquipmentSlot.class);
        registry.add("AnimationController", AnimationController.class);
        registry.add("AnimationState", AnimationState.class);
        registry.add("GeoBone", GeoBone.class);
    }
}