package com.min01.minsanime.mixin;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.minsanime.entity.IShaderEffect;
import com.min01.minsanime.entity.living.EntityFrieren;
import com.min01.minsanime.shader.AnimeEntityEffects;
import com.min01.minsanime.shader.AnimeShaderEffects;
import com.min01.minsanime.shader.AnimeShaderEffects.ZoltraakEffect;
import com.min01.minsanime.util.AnimeClientUtil;
import com.min01.minsanime.util.AnimeUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

@Mixin(value = LevelRenderer.class, priority = -10000)
public class MixinLevelRenderer 
{
    @Inject(method = "Lnet/minecraft/client/renderer/LevelRenderer;initOutline()V", at = @At("TAIL"))
    private void initOutline(CallbackInfo ci)
    {
    	AnimeEntityEffects.ENTITY_PIXEL.initEffect();
    }
    
    @Inject(method = "Lnet/minecraft/client/renderer/LevelRenderer;resize(II)V", at = @At("TAIL"))
    private void resize(int x, int y, CallbackInfo ci)
    {
    	AnimeEntityEffects.ENTITY_PIXEL.resize(x, y);
    }
    
    @Inject(method = "Lnet/minecraft/client/renderer/LevelRenderer;renderLevel(Lcom/mojang/blaze3d/vertex/PoseStack;FJZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lcom/mojang/math/Matrix4f;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OutlineBufferSource;endOutlineBatch()V", shift = At.Shift.BEFORE))
    private void renderLevelProcess(PoseStack poseStack, float frameTime, long l, boolean b, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) 
    {
		for(Entity entity : AnimeUtil.getAllEntities(AnimeClientUtil.MC.level))
		{
			if(!(entity instanceof EntityFrieren))
			{
				continue;
			}
			
	    	poseStack.pushPose();

			Vec3 camPos = camera.getPosition();
			
			double x = Mth.lerp((double)frameTime, entity.xOld, entity.getX());
			double y = Mth.lerp((double)frameTime, entity.yOld, entity.getY());
			double z = Mth.lerp((double)frameTime, entity.zOld, entity.getZ());
			
			Vec3 entityPos = new Vec3(x, y, z);
			Vec3 pos = entityPos.subtract(camPos);
			poseStack.translate(pos.x, pos.y + 1.0F, pos.z);
	    	AnimeEntityEffects.ENTITY_PIXEL.process(poseStack.last().pose());
	    	poseStack.popPose();
		}
    }
    
	@Inject(at = @At(value = "TAIL"), method = "renderLevel")
	private void renderLevel(PoseStack mtx, float frameTime, long nanoTime, boolean renderOutline, Camera camera, GameRenderer gameRenderer, LightTexture light, Matrix4f projMat, CallbackInfo ci)
	{
		RenderSystem.depthMask(false);
		new ArrayList<>(AnimeShaderEffects.EFFECTS).forEach(t -> 
		{
			if(AnimeClientUtil.MC.level.dimension() == t.dimension)
			{
				Vec3 worldPos = t.pos;
				Vec3 camPos = camera.getPosition();
				Vec3 pos = worldPos.subtract(camPos);
				mtx.pushPose();
				mtx.translate(pos.x, pos.y, pos.z);
				if(t.name.equals("Explosion"))
				{
	    			AnimeClientUtil.applyExplosion(mtx, frameTime, t.tickCount, t.scale);
				}
				else if(t.name.equals("ColoredExplosion"))
				{
	    			AnimeClientUtil.applyColoredExplosion(mtx, frameTime, t);
				}
				else if(t instanceof ZoltraakEffect effect)
				{
	    			AnimeClientUtil.applyZoltraak(mtx, frameTime, effect.tickCount, effect.endPos.subtract(camPos), effect.maxScale, effect.rotation);
				}
				mtx.popPose();
			}
		});
		for(Entity entity : AnimeUtil.getAllEntities(AnimeClientUtil.MC.level))
		{
			if(!(entity instanceof IShaderEffect effect) || !effect.shouldApplyEffect())
			{
				continue;
			}
			mtx.pushPose();
			Vec3 camPos = camera.getPosition();
			if(effect.getEffetName().equals("Bullet"))
			{
				double x = Mth.lerp((double)frameTime, entity.xOld, entity.getX());
				double y = Mth.lerp((double)frameTime, entity.yOld, entity.getY());
				double z = Mth.lerp((double)frameTime, entity.zOld, entity.getZ());
				
				Vec3 entityPos = new Vec3(x, y, z);
				Vec3 pos = entityPos.subtract(camPos);
				mtx.translate(pos.x, pos.y + 0.25F, pos.z);
				AnimeClientUtil.applyBullet(mtx, frameTime);
			}
			else if(effect.getEffetName().equals("Light"))
			{
				Vec3 pos = effect.getEffectPosition(entity).subtract(camPos);
				mtx.translate(pos.x, pos.y, pos.z);
				AnimeClientUtil.applyLight(mtx, frameTime);
			}
			mtx.popPose();
		}
		RenderSystem.depthMask(true);

    	AnimeEntityEffects.ENTITY_PIXEL.doEntityEffect();
	}
}
