package quickcarpet.mixin.core;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.UserCache;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quickcarpet.QuickCarpet;
import quickcarpet.annotation.Feature;
import quickcarpet.settings.Settings;

import java.net.Proxy;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow protected abstract void tick(BooleanSupplier booleanSupplier_1);

    // Called during game start
    @Feature("core")
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void onMinecraftServerCTOR(Thread thread, RegistryTracker.Modifiable dimensionTracker, LevelStorage.Session session, SaveProperties saveProperties, ResourcePackManager<ResourcePackProfile> resourcePackManager, Proxy proxy, DataFixer dataFixer, ServerResourceManager serverResourceManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        QuickCarpet.getInstance().dimensionTracker = dimensionTracker;
        QuickCarpet.getInstance().onServerInit((MinecraftServer) (Object) this);
    }

    @Feature("core")
    @Inject(method = "prepareStartRegion", at = @At("RETURN"))
    private void onWorldsLoaded(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
        QuickCarpet.getInstance().onWorldsLoaded((MinecraftServer) (Object) this);
    }

    @Feature("core")
    @Inject(method = "save", at = @At("RETURN"))
    private void onWorldsSaved(boolean silent, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        QuickCarpet.getInstance().onWorldsSaved((MinecraftServer) (Object) this);
    }
    @Feature("core")
    @Inject(method = "tick", at = @At(value = "FIELD", target = "net/minecraft/server/MinecraftServer.ticks:I", shift = At.Shift.AFTER, ordinal = 0))
    private void onTick(BooleanSupplier booleanSupplier_1, CallbackInfo ci) {
        QuickCarpet.getInstance().tick((MinecraftServer) (Object) this);
    }

    @Feature("spawnChunkLevel")
    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 11), require = 1)
    private int adjustSpawnChunkLevel(int level) {
        return Settings.spawnChunkLevel;
    }

    @Feature("spawnChunkLevel")
    @ModifyConstant(method = "prepareStartRegion", constant = @Constant(intValue = 441), require = 1)
    private int adjustSpawnChunkCount(int count) {
        int sideLength = Settings.spawnChunkLevel * 2 - 1;
        return sideLength * sideLength;
    }

    @Inject(method = "shutdown", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;save(ZZZ)Z", shift = At.Shift.AFTER))
    private void onWorldsUnloaded(CallbackInfo ci) {
        QuickCarpet.getInstance().onWorldsUnloaded((MinecraftServer) (Object) this);
    }
}
