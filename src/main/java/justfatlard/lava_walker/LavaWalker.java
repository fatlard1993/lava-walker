package justfatlard.lava_walker;

import justfatlard.pandorical.api.PandoricalApi;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LavaWalker implements ModInitializer {
	public static final String MOD_ID = "lava-walker";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ResourceKey<Enchantment> LAVA_WALKER =
		ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(MOD_ID, "lava_walker"));

	@Override
	public void onInitialize() {
		// Guarded class load: BookOfferDialogue names village-quests types.
		if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("village-quests-justfatlard")) {
			justfatlard.lava_walker.integration.BookOfferDialogue.register();
		}

		if (PandoricalApi.isAvailable()) {
			PandoricalApi.content().registerModAssets(MOD_ID);
		}

		LOGGER.info("Lava Walker enchantment loaded");
	}

	/**
	 * Solidifies lava source blocks around the entity into cobblestone.
	 */
	public static void solidifyLava(LivingEntity entity, Level world, BlockPos pos) {
		if (!entity.onGround()) return;

		BlockState cobblestone = Blocks.COBBLESTONE.defaultBlockState();
		int radius = 2;

		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

		for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-radius, -1, -radius), pos.offset(radius, -1, radius))) {
			if (blockPos.closerToCenterThan(entity.position(), radius)) {
				mutablePos.set(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());

				BlockState stateAbove = world.getBlockState(mutablePos);

				if (!stateAbove.isAir()) continue;

				BlockState state = world.getBlockState(blockPos);
				FluidState fluidState = world.getFluidState(blockPos);

				if (fluidState.getType() == Fluids.LAVA &&
					state.getBlock() instanceof LiquidBlock &&
					fluidState.isSource() &&
					cobblestone.canSurvive(world, blockPos) &&
					world.isUnobstructed(cobblestone, blockPos, CollisionContext.empty())) {

					world.setBlock(blockPos, cobblestone, 3);
					// One square of a crossing. Whether it was a crossing at all is Awards' to
					// decide, from how many came before it and how long ago.
					if (entity instanceof net.minecraft.server.level.ServerPlayer walker) {
						Awards.stepped(walker);
					}
				}
			}
		}
	}
}
