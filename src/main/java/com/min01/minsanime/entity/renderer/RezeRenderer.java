package com.min01.minsanime.entity.renderer;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.entity.model.ModelReze;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RezeRenderer extends MobRenderer<EntityReze, ModelReze>
{
	public final BombDevilRenderer renderer;
	
	public RezeRenderer(Context p_174304_) 
	{
		super(p_174304_, new ModelReze(p_174304_.bakeLayer(ModelReze.LAYER_LOCATION)), 0.5F);
		this.renderer = new BombDevilRenderer(p_174304_);
	}
	
	@Override
	public void render(EntityReze p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_)
	{
		if(p_115455_.isTransformed())
		{
			this.renderer.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
		}
		else
		{
			super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityReze p_115812_)
	{
		return new ResourceLocation(MinsAnime.MODID, "textures/entity/reze.png");
	}
}
