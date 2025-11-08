package com.min01.minsanime.shader;

import java.io.IOException;

import org.joml.Matrix4f;

import com.google.gson.JsonSyntaxException;
import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.util.AnimeClientUtil;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

public class AnimeEntityEffects 
{
	public static final EntityEffect ENTITY_PIXEL = new EntityEffect();
	
	public static class EntityEffect
	{
		public RenderTarget entityTarget;
		public PostChain entityEffect;
		
		public final Minecraft minecraft = AnimeClientUtil.MC;
		
		public void doEntityEffect()
		{
			if(this.shouldShowEntityEffect())
			{
				RenderSystem.enableBlend();
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
				this.entityTarget.blitToScreen(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), false);
				this.entityTarget.clear(Minecraft.ON_OSX);
				this.minecraft.getMainRenderTarget().bindWrite(false);
				RenderSystem.disableBlend();
				RenderSystem.defaultBlendFunc();
			}
		}
		
		public void initEffect()
		{
			if(this.entityEffect != null)
			{
				this.entityEffect.close();
			}
			ResourceLocation location = new ResourceLocation(MinsAnime.MODID, "shaders/post/entity_pixel.json");
			try 
			{
				this.entityEffect = new PostChain(this.minecraft.getTextureManager(), this.minecraft.getResourceManager(), this.minecraft.getMainRenderTarget(), location);
				this.entityEffect.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
				this.entityTarget = this.entityEffect.getTempTarget("final");
			}
			catch(IOException e) 
			{
				e.printStackTrace();
				this.entityEffect = null;
				this.entityTarget = null;
			} 
			catch(JsonSyntaxException e) 
			{
				e.printStackTrace();
				this.entityEffect = null;
				this.entityTarget = null;
			}
		}
		
		public void resize(int p_109488_, int p_109489_) 
		{
			this.minecraft.levelRenderer.needsUpdate();
			if(this.entityEffect != null) 
			{
				this.entityEffect.resize(p_109488_, p_109489_);
			}
		}
		
		public void process(Matrix4f mat)
		{
			EffectInstance shader = this.entityEffect.passes.get(0).getEffect();
			if(shader != null)
			{
				shader.safeGetUniform("InverseTransformMatrix").set(AnimeClientUtil.getInverseTransformMatrix(AnimeClientUtil.INVERSE_MAT, mat));
				shader.safeGetUniform("InverseProjectionMatrix").set(RenderSystem.getProjectionMatrix().invert());
				shader.safeGetUniform("InverseViewMatrix").set(mat.invert());
				shader.safeGetUniform("iResolution").set((float) this.minecraft.getWindow().getWidth(), (float) this.minecraft.getWindow().getHeight());
				shader.safeGetUniform("iTime").set((((float) (this.minecraft.level.getGameTime() % 2400000)) + this.minecraft.getFrameTime()) / 20.0F);
			}
			this.entityEffect.process(this.minecraft.getFrameTime());
			this.minecraft.getMainRenderTarget().bindWrite(false);
		}
		
		public boolean shouldShowEntityEffect()
		{
			return this.entityTarget != null && this.entityEffect != null && this.minecraft.player != null;
		}
	}
}