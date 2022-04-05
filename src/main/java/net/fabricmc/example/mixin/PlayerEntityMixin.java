package net.fabricmc.example.mixin;

import net.fabricmc.example.accessors.PlayerAcessors;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerAcessors {


	@Shadow @Final private PlayerAbilities abilities;


	@Unique
	private int InteractTimer = 0;

	@Inject(method = "tick", at = @At(value = "HEAD"))
	public void useOnBlock(CallbackInfo ci) {


		if(this.interactTimer() == 0) {
			this.abilities.allowModifyWorld = true;
		}
		if(this.interactTimer() != 0) {
			this.abilities.allowModifyWorld = false;
		}
		if(this.interactTimer() > 0 ){
			InteractTimer--;
		}

	}

	@Inject(method = "isBlockBreakingRestricted", at = @At(value = "HEAD"),cancellable = true)
	public void isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
		if(this.interactTimer() != 0) {
			cir.setReturnValue(true);
		}
	}

	@Override
	public void setInteractTimer(int bounced) {
		this.InteractTimer = bounced;
	}

	@Override
	public int interactTimer() {
		return InteractTimer;
	}

}
