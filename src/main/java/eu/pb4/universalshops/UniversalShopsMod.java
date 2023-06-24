package eu.pb4.universalshops;

import com.mojang.logging.LogUtils;
import eu.pb4.polymer.networking.api.PolymerServerNetworking;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import eu.pb4.universalshops.gui.setup.SelectItemGui;
import eu.pb4.universalshops.compat.FlanCompat;
import eu.pb4.universalshops.other.USCommands;
import eu.pb4.universalshops.registry.USRegistry;
import eu.pb4.universalshops.trade.PriceHandler;
import eu.pb4.universalshops.trade.StockHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

public class UniversalShopsMod implements ModInitializer {
    public static final String MOD_ID = "universal_shops";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final boolean IS_DEV = FabricLoader.getInstance().isDevelopmentEnvironment();

    public static final Identifier HELLO_PACKET = id("hello");

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        FlanCompat.register();
        GenericModInfo.build(FabricLoader.getInstance().getModContainer(MOD_ID).get());
        USRegistry.register();
        PriceHandler.init();
        StockHandler.init();
        ServerLifecycleEvents.SERVER_STARTED.register(server -> SelectItemGui.updateCachedItems(server));
        CommandRegistrationCallback.EVENT.register(USCommands::register);

        PolymerServerNetworking.registerSendPacket(HELLO_PACKET, 0);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            PolymerResourcePackUtils.addModAssets(MOD_ID);
        }
    }
}
