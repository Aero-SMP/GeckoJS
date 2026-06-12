package org.clockwork.neogeckojs;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod(GeckoJS.MODID)
public class GeckoJS {
    public static final String MODID = "geckojs";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static final Map<ResourceLocation, ExtendedGeoModel<?>> REGISTERED_BLOCKS = new HashMap<>();
    public static final List<ResourceLocation> REGISTERED_SHIELDS = new ArrayList<>();

    public GeckoJS() {
    }
}