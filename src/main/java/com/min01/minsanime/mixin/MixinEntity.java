package com.min01.minsanime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.minsanime.util.AnimeUtil;

import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public class MixinEntity 
{
    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci)
    {
    	Entity entity = Entity.class.cast(this);
    	AnimeUtil.tickOwner(entity);
    }
    
	@Inject(at = @At(value = "HEAD"), method = "isAlliedTo(Lnet/minecraft/world/entity/Entity;)Z", cancellable = true)
	private void isAlliedTo(Entity p_147185_, CallbackInfoReturnable<Boolean> cir)
	{
		Entity entity = Entity.class.cast(this);
		if(AnimeUtil.getOwner(entity) != null)
		{
			if(AnimeUtil.getOwner(entity) == p_147185_)
			{
				cir.setReturnValue(true);
			}
			if(AnimeUtil.getOwner(p_147185_) != null)
			{
				if(AnimeUtil.getOwner(p_147185_) == AnimeUtil.getOwner(entity))
				{
					cir.setReturnValue(true);
				}
			}
		}
	}
}
