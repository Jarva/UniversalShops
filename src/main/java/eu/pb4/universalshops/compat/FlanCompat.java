package eu.pb4.universalshops.compat;

import eu.pb4.universalshops.registry.TradeShopBlock;
import eu.pb4.universalshops.registry.USRegistry;
import io.github.flemmli97.flan.api.ClaimHandler;
import io.github.flemmli97.flan.api.data.IPermissionContainer;
import io.github.flemmli97.flan.api.data.IPermissionStorage;
import io.github.flemmli97.flan.api.permission.ClaimPermission;
import io.github.flemmli97.flan.api.permission.ObjectToPermissionMap;
import io.github.flemmli97.flan.api.permission.PermissionRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class FlanCompat {
    public static final FabricLoader loader = FabricLoader.getInstance();

    public static ClaimPermission USESHOP;

    public static void register() {
        if (loader.isModLoaded("flan")) {
            USESHOP = PermissionRegistry.registerBlockInteract(new ClaimPermission("USESHOP", () -> new ItemStack(USRegistry.ITEM), true, "Permission to use trade shops"));
            ObjectToPermissionMap.registerBlockPredicateMap(block -> block instanceof TradeShopBlock, () -> USESHOP);
        }
    }

    public static boolean canInteract(ServerWorld world, ServerPlayerEntity player, BlockPos pos, ClaimPermission type) {
        IPermissionStorage storage = ClaimHandler.getPermissionStorage(world);
        IPermissionContainer container = storage.getForPermissionCheck(pos);

        return container.canInteract(player, type, pos);
    }
}
