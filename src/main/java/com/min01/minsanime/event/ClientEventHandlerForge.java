package com.min01.minsanime.event;

import java.util.ArrayList;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.EntityCameraShake;
import com.min01.minsanime.entity.IShaderEffect;
import com.min01.minsanime.shader.AnimeShaderEffects;
import com.min01.minsanime.util.AnimeClientUtil;
import com.min01.minsanime.util.AnimeUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinsAnime.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandlerForge
{
	
    @SubscribeEvent
    public static void onSetupCamera(ViewportEvent.ComputeCameraAngles event) 
    {
        Player player = AnimeClientUtil.MC.player;
        float delta = AnimeClientUtil.MC.getFrameTime();
        float ticksExistedDelta = player.tickCount + delta;
        if(player != null)
        {
            float shakeAmplitude = 0.0F;
            for(EntityCameraShake cameraShake : player.level.getEntitiesOfClass(EntityCameraShake.class, player.getBoundingBox().inflate(100.0))) 
            {
                if(cameraShake.distanceTo(player) < cameraShake.getRadius())
                {
                    shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                }
            }
            if(shakeAmplitude > 1.0F)
            {
                shakeAmplitude = 1.0F;
            }
            event.setPitch((float)(event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3.0F + 2.0F) * 25.0));
            event.setYaw((float)(event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5.0F + 1.0F) * 25.0));
            event.setRoll((float)(event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4.0F) * 25.0));
        }
    }
    
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) 
    {
        if(event.phase == TickEvent.Phase.START && AnimeClientUtil.MC.player != null && AnimeClientUtil.MC.level != null) 
        {
        	if(!AnimeClientUtil.MC.isPaused())
        	{
            	AnimeShaderEffects.EFFECTS.removeIf(t -> !t.isAlive());
        	}
        }
    }
    
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event)
    {
    	if(event.getStage() == Stage.AFTER_WEATHER)
    	{
    		Camera camera = event.getCamera();
    		PoseStack mtx = event.getPoseStack();
    		float frameTime = event.getPartialTick();
    		
    		RenderSystem.depthMask(false);
    		new ArrayList<>(AnimeShaderEffects.EFFECTS).forEach(t -> 
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
    			else if(t.name.equals("Light"))
    			{
        			AnimeClientUtil.applyLight(mtx, frameTime, t.tickCount);
    			}
    			else if(t.name.equals("ColoredExplosion"))
    			{
        			AnimeClientUtil.applyColoredExplosion(mtx, frameTime, t.tickCount, t.scale, t.color);
    			}
    			mtx.popPose();
    		});
    		for(Entity entity : AnimeUtil.getAllEntities(AnimeClientUtil.MC.level))
    		{
    			if(!(entity instanceof IShaderEffect))
    			{
    				continue;
    			}
    			
    			double x = Mth.lerp((double)frameTime, entity.xOld, entity.getX());
    			double y = Mth.lerp((double)frameTime, entity.yOld, entity.getY());
    			double z = Mth.lerp((double)frameTime, entity.zOld, entity.getZ());
    			
    			Vec3 camPos = camera.getPosition();
    			Vec3 entityPos = new Vec3(x, y, z);
    			Vec3 pos = entityPos.subtract(camPos);
    			mtx.pushPose();
    			mtx.translate(pos.x, pos.y + 0.25F, pos.z);
    			AnimeClientUtil.applyBullet(mtx, frameTime);
    			mtx.popPose();
    		}
    		RenderSystem.depthMask(true);
    	}
    }
}
