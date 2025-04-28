package com.min01.minsanime.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.minsanime.shader.ShaderEffectHandler;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer 
{
	@Inject(at = @At(value = "TAIL"), method = "initOutline", cancellable = true)
	private void initOutline(CallbackInfo ci)
	{
		ShaderEffectHandler.init();
	}

	@Inject(at = @At(value = "TAIL"), method = "resize", cancellable = true)
	private void resize(int p_109488_, int p_109489_, CallbackInfo ci)
	{
		ShaderEffectHandler.resize(p_109488_, p_109489_);
	}
	
	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OutlineBufferSource;endOutlineBatch()V", shift = At.Shift.BEFORE))
	private void renderLevel(PoseStack p_109600_, float p_109601_, long p_109602_, boolean p_109603_, Camera p_109604_, GameRenderer p_109605_, LightTexture p_109606_, Matrix4f p_254120_, CallbackInfo ci)
	{
		ShaderEffectHandler.process();
	}

	@Inject(method = "renderLevel", at = @At(value = "TAIL"))
	private void renderLevelTail(PoseStack p_109600_, float p_109601_, long p_109602_, boolean p_109603_, Camera p_109604_, GameRenderer p_109605_, LightTexture p_109606_, Matrix4f p_254120_, CallbackInfo ci)
	{
		ShaderEffectHandler.blit();
	}
}
