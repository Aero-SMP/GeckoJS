package org.clockwork.neogeckojs;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.entity.BlockEntityBuilder;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class AnimatableBlockBuilder extends BlockBuilder {
    public final transient AnimatableBlockEntityInfo blockEntityInfo = new AnimatableBlockEntityInfo(this);
    public final ExtendedGeoModel<AnimatableBlockEntity> blockModel = new ExtendedGeoModel<>();

    public AnimatableBlockBuilder(ResourceLocation id) {
        super(id);
        this.opaque = false;
    }

    @Info("Creates an animatable Block Entity for this block")
    public AnimatableBlockBuilder animatableBlockEntity(Consumer<AnimatableBlockEntityInfo> consumer) {
        consumer.accept(blockEntityInfo);
        return this;
    }

    public AnimatableBlockBuilder addAnimation(AnimatableBlockEntityInfo.AnimationStateCallback callback) {
        blockEntityInfo.addAnimation(callback);
        return this;
    }

    public AnimatableBlockBuilder addController(Consumer<AnimationControllerBuilder<AnimatableBlockEntity>> consumer) {
        blockEntityInfo.addController(consumer);
        return this;
    }

    public AnimatableBlockBuilder geoModel(Consumer<ExtendedGeoModel.Builder<AnimatableBlockEntity>> consumer) {
        consumer.accept(blockModel.builder);
        return this;
    }

    public AnimatableBlockBuilder defaultGeoModel() {
        blockModel.builder.setSimpleModel(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "geo/block/" + id.getPath() + ".geo.json"));
        blockModel.builder.setSimpleTexture(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "textures/block/" + id.getPath() + ".png"));
        blockModel.builder.setSimpleAnimation(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "animations/block/" + id.getPath() + ".animation.json"));
        return this;
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        registry.add(Registries.BLOCK_ENTITY_TYPE, new BlockEntityBuilder(id, blockEntityInfo));
        GeckoJS.REGISTERED_BLOCKS.put(id, blockModel);
    }

    @Override
    public Block createObject() {
        return new AnimatableBlock(this);
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        // Blocks with GeoModels usually don't need standard block models except for particles
        generator.blockModel(id, model -> {
            model.parent(ResourceLocation.fromNamespaceAndPath("minecraft", "block/block"));
            model.texture("particle", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath()).toString());
        });
        // You might want to generate a simple blockstate too
        generator.blockState(id, state -> {
            state.variant("", v -> v.model(id));
        });
    }
}
