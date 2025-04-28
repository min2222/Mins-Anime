package com.min01.minsanime.shader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonSyntaxException;
import com.min01.minsanime.util.AnimeClientUtil;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;

public class ShaderEffectHandler 
{
	public static final List<ResourceLocation> REGISTRY = new ArrayList<>();
	public static final Map<ResourceLocation, ShaderEffect> EFFECTS = new HashMap<>();
	
    public static void clear()
    {
        for(ShaderEffect effect : EFFECTS.values())
        {
        	effect.close();
        }
        EFFECTS.clear();
    }

    public static void registerEffect(ResourceLocation location)
    {
        REGISTRY.add(location);
    }
    
    public static void resize(int x, int y)
    {
        for(ShaderEffect effect : EFFECTS.values())
        {
        	effect.resize(x, y);
        }
    }
    
    public static void renderEffect(ResourceLocation location)
    {
        ShaderEffect effect = EFFECTS.get(location);
        if(effect != null)
        {
            effect.enabled = true;
        }
    }
    
    public static void init()
    {
        clear();
        for(ResourceLocation location : REGISTRY)
        {
            PostChain postChain;
            RenderTarget renderTarget;
            try 
            {
                postChain = new PostChain(AnimeClientUtil.MC.getTextureManager(), AnimeClientUtil.MC.getResourceManager(), AnimeClientUtil.MC.getMainRenderTarget(), location);
                postChain.resize(AnimeClientUtil.MC.getWindow().getWidth(), AnimeClientUtil.MC.getWindow().getHeight());
                renderTarget = postChain.getTempTarget("final");
            } 
            catch(IOException ioexception) 
            {
                postChain = null;
                renderTarget = null;
            }
            catch(JsonSyntaxException jsonsyntaxexception)
            {
                postChain = null;
                renderTarget = null;
            }
            EFFECTS.put(location, new ShaderEffect(postChain, renderTarget, false));
        }
    }

    public static void blit()
    {
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        for(ShaderEffect effect : EFFECTS.values())
        {
            if(effect.postChain != null && effect.enabled)
            {
            	effect.renderTarget.blitToScreen(AnimeClientUtil.MC.getWindow().getWidth(), AnimeClientUtil.MC.getWindow().getHeight(), false);
            	effect.enabled = false;
            	effect.renderTarget.clear(Minecraft.ON_OSX);
                AnimeClientUtil.MC.getMainRenderTarget().bindWrite(false);
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }
    
    public static void process() 
    {
        for(ShaderEffect postEffect : EFFECTS.values()) 
        {
            if(postEffect.enabled && postEffect.postChain != null)
            {
                postEffect.postChain.process(AnimeClientUtil.MC.getFrameTime());
                AnimeClientUtil.MC.getMainRenderTarget().bindWrite(false);
            }
        }
    }
    
    public static RenderTarget getRenderTarget(ResourceLocation location)
    {
        ShaderEffect effect = EFFECTS.get(location);
        return effect == null ? null : effect.renderTarget;
    }
	
	public static class ShaderEffect
	{
		public boolean enabled;
		public RenderTarget renderTarget;
		public PostChain postChain;

        public ShaderEffect(PostChain postChain, RenderTarget renderTarget, boolean enabled) 
        {
        	this.enabled = enabled;
        	this.postChain = postChain;
        	this.renderTarget = renderTarget;
		}

		public void close()
        {
            if(this.postChain != null)
            {
            	this.postChain.close();
            }
        }

        public void resize(int x, int y)
        {
            if(this.postChain != null)
            {
            	this.postChain.resize(x, y);
            }
        }
	}
}
