package com.min01.minsanime.mixin;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.shader.AnimeShaderEffects;
import com.min01.minsanime.shader.AnimeShaders;
import com.min01.minsanime.shader.ExtendedPostChain;
import com.min01.minsanime.util.AnimeClientUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer 
{
	private final Matrix4f inverseMat = new Matrix4f();

	@Inject(at = @At(value = "TAIL"), method = "renderLevel")
	private void renderLevel(PoseStack mtx, float frameTime, long nanoTime, boolean renderOutline, Camera camera, GameRenderer gameRenderer, LightTexture light, Matrix4f projMat, CallbackInfo ci)
	{
		RenderSystem.depthMask(false);
		new ArrayList<>(AnimeShaderEffects.EFFECTS).forEach(t -> 
		{
			Vec3 worldPos = t.pos;
			Vec3 camPos = camera.getPosition();
			Vec3 pos = worldPos.subtract(camPos);
			mtx.pushPose();
			mtx.translate(pos.x, pos.y, pos.z);
			this.apply(mtx, frameTime, t.tickCount);
			mtx.popPose();
		});
		RenderSystem.depthMask(true);
	}
	
	public void apply(PoseStack mtx, float frameTime, int tickCount)
	{
		Minecraft minecraft = AnimeClientUtil.MC;

		ExtendedPostChain shaderChain = AnimeShaders.getExplosion();
		EffectInstance shader = shaderChain.getMainShader();

		if(shader != null)
		{
			shader.safeGetUniform("iResolution").set(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
			shader.setSampler("iChannel0", () -> minecraft.getTextureManager().getTexture(new ResourceLocation(MinsAnime.MODID, "textures/misc/organic4.png")).getId());
			shader.setSampler("iChannel1", () -> minecraft.getTextureManager().getTexture(new ResourceLocation(MinsAnime.MODID, "textures/misc/rgba_noise_medium.png")).getId());
			shader.safeGetUniform("InverseTransformMatrix").set(getInverseTransformMatrix(this.inverseMat, mtx.last().pose()));
			shader.safeGetUniform("iTime").set(tickCount / 20.0F);
			shaderChain.process(frameTime);
			minecraft.getMainRenderTarget().bindWrite(false);
		}
	}
	
	private static Matrix4f getInverseTransformMatrix(Matrix4f outMat, Matrix4f modelView)
    {
		return outMat.identity().mul(RenderSystem.getProjectionMatrix()).mul(modelView).invert();
    }
}
