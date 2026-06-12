package org.clockwork.neogeckojs;

import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class AnimatableBlockEntityRenderer<T extends BlockEntity & GeoBlockEntity> extends GeoBlockRenderer<T> {
    public AnimatableBlockEntityRenderer(ExtendedGeoModel<T> model) {
        super(model);
        this.scaleWidth = model.builder.scaleWidth;
        this.scaleHeight = model.builder.scaleHeight;
        if (model.builder.autoGlowing) {
            addRenderLayer(new AutoGlowingGeoLayer<>(this));
        }
    }
}
