package justfatlard.lava_walker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import justfatlard.lava_walker.LavaWalker;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Unique
	private BlockPos lavaWalker$lastPos;

	@Inject(method = "aiStep", at = @At("TAIL"))
	private void onTickMovement(CallbackInfo ci) {
		LivingEntity self = (LivingEntity)(Object)this;
		Level world = self.level();

		if (world.isClientSide()) return;

		BlockPos pos = self.blockPosition();
		ItemStack boots = self.getItemBySlot(EquipmentSlot.FEET);

		if (boots.isEmpty()) return;
		if (pos.equals(lavaWalker$lastPos)) return;

		var enchantmentRegistry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
		var lavaWalkerOpt = enchantmentRegistry.get(LavaWalker.LAVA_WALKER);

		if (lavaWalkerOpt.isEmpty()) return;

		int level = EnchantmentHelper.getItemEnchantmentLevel(lavaWalkerOpt.get(), boots);

		if (level > 0) {
			lavaWalker$lastPos = pos.immutable();
			LavaWalker.solidifyLava(self, world, pos);
		}
	}
}
