package com.min01.minsanime.entity.renderer;

import java.util.ArrayList;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityFrieren;
import com.min01.minsanime.entity.living.EntityFrieren.LaserType;
import com.min01.minsanime.entity.model.ModelFrieren;
import com.min01.minsanime.misc.AnimeRenderType;
import com.min01.minsanime.util.AnimeClientUtil;
import com.min01.minsanime.util.AnimeUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class FrierenRenderer extends MobRenderer<EntityFrieren, ModelFrieren>
{
	public FrierenRenderer(Context p_174304_)
	{
		super(p_174304_, new ModelFrieren(p_174304_.bakeLayer(ModelFrieren.LAYER_LOCATION)), 0.5F);
	}
	
	@Override
	public void render(EntityFrieren p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_)
	{
		super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
		new ArrayList<>(p_115455_.zoltraak).forEach(t -> 
		{
			p_115458_.pushPose();
			
			Vec2 rot = t.rotation;
			Vec3 pos = t.position.subtract(p_115455_.getPosition(p_115457_));
			p_115458_.translate(pos.x, pos.y, pos.z);
			p_115458_.mulPose(Axis.YP.rotationDegrees(-rot.y + 180.0F));
			p_115458_.mulPose(Axis.XP.rotationDegrees(-rot.x));
			
			p_115458_.pushPose();
			p_115458_.mulPose(Axis.XP.rotationDegrees(-90.0F));
	        Vec3 color = Vec3.fromRGB24(15659769);
	        Vec3 start = AnimeUtil.getLookPos(rot, Vec3.ZERO, 0.0F, 0.5F, 0.0F);
	        if(t.type == LaserType.BIG)
	        {
	        	start = AnimeUtil.getLookPos(rot, Vec3.ZERO, 0.0F, 3.5F, 0.0F);
	        }
	        Vec3 end = t.getEndPos(p_115457_, p_115455_.level);
	        end = end.yRot((float) Math.toRadians(rot.y - 180.0F)).xRot((float) Math.toRadians(-rot.x));
	        end = new Vec3(end.x, -end.z, end.y);
	        AnimeClientUtil.drawCurvedCylinder(p_115458_, p_115459_.getBuffer(AnimeRenderType.zoltraak(new ResourceLocation(MinsAnime.MODID, "textures/effect/white.png"))), start, end, t.laserSize, t.distance, 32, 32, (float)color.x, (float)color.y, (float)color.z, 1.0F);
			p_115458_.popPose();

			p_115458_.pushPose();
			p_115458_.mulPose(Axis.ZP.rotationDegrees(p_115455_.tickCount / 2));
			AnimeClientUtil.drawQuad(p_115458_, p_115459_.getBuffer(AnimeRenderType.eyesNoAlpha(new ResourceLocation(MinsAnime.MODID, "textures/effect/zoltraak.png"))), t.circleSize, LightTexture.FULL_BRIGHT, 1.0F);
			p_115458_.popPose();
			
			p_115458_.popPose();
		});
	}

	@Override
	public ResourceLocation getTextureLocation(EntityFrieren p_115812_)
	{
		return new ResourceLocation(MinsAnime.MODID, "textures/entity/frieren.png");
	}
}
