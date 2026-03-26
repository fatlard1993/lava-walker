package justfatlard.lava_walker;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LavaWalker implements ModInitializer {
	public static final String MOD_ID = "lava-walker";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RegistryKey<net.minecraft.enchantment.Enchantment> LAVA_WALKER =
		RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "lava_walker"));

	@Override
	public void onInitialize() {
		PolymerResourcePackUtils.addModAssets(MOD_ID);
		PolymerResourcePackUtils.markAsRequired();

		LOGGER.info("Lava Walker enchantment loaded");
	}

	/**
	 * Solidifies lava source blocks around the entity into cobblestone.
	 */
	public static void solidifyLava(LivingEntity entity, World world, BlockPos pos) {
		if (!entity.isOnGround()) return;

		BlockState cobblestone = Blocks.COBBLESTONE.getDefaultState();
		int radius = 2;

		BlockPos.Mutable mutablePos = new BlockPos.Mutable();

		for (BlockPos blockPos : BlockPos.iterate(pos.add(-radius, -1, -radius), pos.add(radius, -1, radius))) {
			if (blockPos.isWithinDistance(entity.getEntityPos(), radius)) {
				mutablePos.set(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());

				BlockState stateAbove = world.getBlockState(mutablePos);

				if (!stateAbove.isAir()) continue;

				BlockState state = world.getBlockState(blockPos);
				FluidState fluidState = world.getFluidState(blockPos);

				if (fluidState.getFluid() == Fluids.LAVA &&
					state.getBlock() instanceof FluidBlock &&
					fluidState.isStill() &&
					cobblestone.canPlaceAt(world, blockPos) &&
					world.canPlace(cobblestone, blockPos, ShapeContext.absent())) {

					world.setBlockState(blockPos, cobblestone);
				}
			}
		}
	}
}
