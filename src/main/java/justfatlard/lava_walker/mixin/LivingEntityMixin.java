package justfatlard.lava_walker.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import justfatlard.lava_walker.LavaWalker;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

	@Inject(method = "tickMovement", at = @At("TAIL"))
	private void onTickMovement(CallbackInfo ci) {
		LivingEntity self = (LivingEntity)(Object)this;
		World world = self.getEntityWorld();

		if (world.isClient()) return;

		BlockPos pos = self.getBlockPos();
		ItemStack boots = self.getEquippedStack(EquipmentSlot.FEET);

		if (boots.isEmpty()) return;

		// Get the enchantment from registry
		var enchantmentRegistry = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
		var lavaWalkerOpt = enchantmentRegistry.getOptional(LavaWalker.LAVA_WALKER);

		if (lavaWalkerOpt.isEmpty()) return;

		int level = EnchantmentHelper.getLevel(lavaWalkerOpt.get(), boots);

		if (level > 0) {
			LavaWalker.solidifyLava(self, world, pos, level);
		}
	}
}
