package com.min01.minsanime.entity.renderer;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.entity.model.ModelBombDevil;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class BombDevilRenderer extends MobRenderer<EntityReze, ModelBombDevil>
{
	public BombDevilRenderer(Context p_174304_) 
	{
		super(p_174304_, new ModelBombDevil(p_174304_.bakeLayer(ModelBombDevil.LAYER_LOCATION)), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(EntityReze p_115812_)
	{
		return new ResourceLocation(MinsAnime.MODID, "textures/entity/bomb_devil.png");
	}
}
