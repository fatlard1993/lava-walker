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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class LavaWalker implements ModInitializer {
	public static final String MOD_ID = "lava-walker";

	public static final RegistryKey<net.minecraft.enchantment.Enchantment> LAVA_WALKER =
		RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "lava_walker"));

	@Override
	public void onInitialize() {
		// Register mod assets (lang files) with Polymer for vanilla clients
		PolymerResourcePackUtils.addModAssets(MOD_ID);
		PolymerResourcePackUtils.markAsRequired();

		System.out.println("[lava-walker] Lava Walker enchantment loaded");
	}

	/**
	 * Solidifies lava around the entity, similar to FrostWalkerEnchantment.freezeWater
	 */
	public static void solidifyLava(LivingEntity entity, World world, BlockPos pos, int level) {
		if (!entity.isOnGround()) return;

		BlockState cobblestone = Blocks.COBBLESTONE.getDefaultState();
		// Small radius since cobblestone is permanent
		int radius = 2;

		BlockPos.Mutable mutablePos = new BlockPos.Mutable();

		for (BlockPos blockPos : BlockPos.iterate(pos.add(-radius, -1, -radius), pos.add(radius, -1, radius))) {
			if (blockPos.isWithinDistance(entity.getBlockPos().toCenterPos(), radius)) {
				mutablePos.set(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());

				BlockState stateAbove = world.getBlockState(mutablePos);

				if (!stateAbove.isAir()) continue;

				BlockState state = world.getBlockState(blockPos);
				FluidState fluidState = world.getFluidState(blockPos);

				// Check if it's a full lava source block
				if (fluidState.getFluid() == Fluids.LAVA &&
					state.getBlock() instanceof FluidBlock &&
					fluidState.isStill() &&
					cobblestone.canPlaceAt(world, blockPos) &&
					world.canPlace(cobblestone, blockPos, ShapeContext.absent())) {

					world.setBlockState(blockPos, cobblestone);
					world.scheduleBlockTick(blockPos, Blocks.COBBLESTONE, MathHelper.nextInt(entity.getRandom(), 60, 120));
				}
			}
		}
	}
}
