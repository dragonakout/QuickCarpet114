package quickcarpet.mixin.skyblock;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.TheNetherDimension;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.level.LevelGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import quickcarpet.helper.SkyBlock;

@Mixin(TheNetherDimension.class)
public abstract class MixinTheNetherDimension extends Dimension {

    public MixinTheNetherDimension(World world, DimensionType type) {
        super(world, type);
    }

    @Inject(method = "createChunkGenerator()Lnet/minecraft/world/gen/chunk/ChunkGenerator;", at = @At("HEAD"), cancellable = true)
    private void createSkyBlockGenerator(CallbackInfoReturnable<ChunkGenerator<? extends ChunkGeneratorConfig>> cir) {
        LevelGeneratorType type = this.world.getLevelProperties().getGeneratorType();
        if (type == SkyBlock.LEVEL_GENERATOR_TYPE) {
            cir.setReturnValue(SkyBlock.createNetherChunkGenerator(this.world));
            cir.cancel();
        }
    }
}