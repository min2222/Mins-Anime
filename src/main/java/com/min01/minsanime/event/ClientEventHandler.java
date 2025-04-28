package com.min01.minsanime.event;

import java.util.function.Consumer;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.AnimeEntities;
import com.min01.minsanime.entity.model.ModelAltair;
import com.min01.minsanime.entity.model.ModelAltairSabre;
import com.min01.minsanime.entity.model.ModelFrieren;
import com.min01.minsanime.entity.renderer.AltairRenderer;
import com.min01.minsanime.entity.renderer.AltairSabreRenderer;
import com.min01.minsanime.entity.renderer.FrierenRenderer;
import com.min01.minsanime.misc.AnimeRenderType;
import com.min01.minsanime.obj.ObjModelManager;
import com.min01.minsanime.particle.AnimeParticles;
import com.min01.minsanime.particle.SparkParticle;
import com.min01.minsanime.shader.AnimeShaders;
import com.min01.minsanime.shader.ShaderEffectHandler;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MinsAnime.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler 
{
	@SubscribeEvent
	public static void onFMLClientSetup(FMLClientSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.register(ObjModelManager.getInstance());
		ShaderEffectHandler.registerEffect(AnimeShaders.BLUR);
	}
	
    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event)
    {
        for(Pair<ShaderInstance, Consumer<ShaderInstance>> pair : AnimeRenderType.registerShaders(event.getResourceProvider())) 
        {
            event.registerShader(pair.getFirst(), pair.getSecond());
        }
    }
    
	@SubscribeEvent
	public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event)
	{
		event.registerSpecial(AnimeParticles.SPARK.get(), new SparkParticle.Provider());
	}
	
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
    	event.registerLayerDefinition(ModelAltair.LAYER_LOCATION, ModelAltair::createBodyLayer);
    	event.registerLayerDefinition(ModelAltairSabre.LAYER_LOCATION, ModelAltairSabre::createBodyLayer);
    	event.registerLayerDefinition(ModelFrieren.LAYER_LOCATION, ModelFrieren::createBodyLayer);
    }
    
    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
    	event.registerEntityRenderer(AnimeEntities.ALTAIR.get(), AltairRenderer::new);
    	event.registerEntityRenderer(AnimeEntities.ALTAIR_SABRE.get(), AltairSabreRenderer::new);
    	event.registerEntityRenderer(AnimeEntities.FRIEREN.get(), FrierenRenderer::new);
    }
}
