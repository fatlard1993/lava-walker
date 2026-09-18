package justfatlard.lava_walker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

/**
 * The advancements this mod hands out.
 *
 * <p>Crossing a lake is not one event, it is a lot of small ones in a row, and the difference
 * between a crossing and stepping over a puddle at the edge is only how many. So the steps are
 * counted while they keep coming: a run of them close together is a walk out over open lava, and a
 * gap long enough to have been anything else starts the count again.
 *
 * <p>Counted in memory and not saved. A server restart mid-lake loses your tally, which costs
 * nothing worth the bookkeeping: walk out again.
 */
public final class Awards {
	private Awards() {}

	/** Steps taken over lava, in a row, that make a crossing rather than a paddle. */
	private static final int A_CROSSING = 24;

	/** How long a gap may be before the walk counts as finished. */
	private static final long KEEPS_GOING_MILLIS = 6_000L;

	private record Walk(int steps, long lastStep) {}

	private static final Map<UUID, Walk> walks = new HashMap<>();

	/** One more square of lava went solid under this player's boots. */
	public static void stepped(ServerPlayer walker) {
		long now = System.currentTimeMillis();
		Walk walk = walks.get(walker.getUUID());
		int steps = walk != null && now - walk.lastStep() <= KEEPS_GOING_MILLIS ? walk.steps() + 1 : 1;
		walks.put(walker.getUUID(), new Walk(steps, now));
		if (steps < A_CROSSING) return;

		walks.remove(walker.getUUID());
		award(walker, "crossing");
	}

	private static void award(ServerPlayer player, String path) {
		if (player.level().getServer() == null) return;
		AdvancementHolder holder = player.level().getServer().getAdvancements()
			.get(Identifier.fromNamespaceAndPath(LavaWalker.MOD_ID, path));
		if (holder == null) return;

		AdvancementProgress progress = player.getAdvancements().getOrStartProgress(holder);
		if (progress.isDone()) return;
		for (String criterion : progress.getRemainingCriteria()) {
			player.getAdvancements().award(holder, criterion);
		}
	}
}
