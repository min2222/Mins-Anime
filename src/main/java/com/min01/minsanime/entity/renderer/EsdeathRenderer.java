package com.min01.minsanime.entity.renderer;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityEsdeath;
import com.min01.minsanime.entity.model.ModelEsdeath;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class EsdeathRenderer extends MobRenderer<EntityEsdeath, ModelEsdeath>
{
	public EsdeathRenderer(Context p_174304_) 
	{
		super(p_174304_, new ModelEsdeath(p_174304_.bakeLayer(ModelEsdeath.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityEsdeath p_115812_)
	{
		return new ResourceLocation(MinsAnime.MODID, "textures/entity/esdeath.png");
	}
}
