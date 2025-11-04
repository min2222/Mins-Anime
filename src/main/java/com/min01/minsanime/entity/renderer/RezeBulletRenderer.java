package com.min01.minsanime.entity.renderer;

import com.min01.minsanime.entity.projectile.EntityRezeBullet;
import com.min01.minsanime.util.AnimeClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class RezeBulletRenderer extends EntityRenderer<EntityRezeBullet>
{
	public RezeBulletRenderer(Context p_174008_)
	{
		super(p_174008_);
	}
	
	@Override
	public void render(EntityRezeBullet p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) 
	{
		if(p_114485_.hasTrail())
		{
			p_114488_.pushPose();
			Vec3 pos = p_114485_.getPosition(p_114487_);
			p_114488_.translate(-pos.x, -pos.y + 0.25F, -pos.z);
	        Vec3 color = Vec3.fromRGB24(16730624);
	        AnimeClientUtil.renderTrail(p_114485_, p_114487_, p_114488_, p_114489_, (float)color.x, (float)color.y, (float)color.z, 0.8F, 5, 0.05F);
	        p_114488_.popPose();
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityRezeBullet p_114482_)
	{
		return null;
	}
}
