package quickcarpet.mixin.renewableCoral;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.CoralFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import quickcarpet.annotation.Feature;
import quickcarpet.utils.extensions.ExtendedCoralFeature;

import java.util.Random;

@Feature("renewableCoral")
@Mixin(CoralFeature.class)
public abstract class CoralFeatureMixin implements ExtendedCoralFeature
{
    @Shadow protected abstract boolean spawnCoral(WorldAccess var1, Random var2, BlockPos var3, BlockState var4);

    @Override
    public boolean growSpecific(World worldIn, Random random, BlockPos pos, BlockState blockUnder)
    {
        return spawnCoral(worldIn, random, pos, blockUnder);
    }
}
