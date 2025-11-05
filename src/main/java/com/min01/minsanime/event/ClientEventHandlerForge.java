package com.min01.minsanime.event;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.EntityCameraShake;
import com.min01.minsanime.shader.AnimeShaderEffects;
import com.min01.minsanime.util.AnimeClientUtil;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
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
}
