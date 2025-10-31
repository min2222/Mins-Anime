package com.min01.minsanime.entity.renderer;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.projectile.EntityIce;
import com.min01.minsanime.misc.AnimeRenderType;
import com.min01.minsanime.util.AnimeClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class IceRenderer extends EntityRenderer<EntityIce>
{
	public static final ResourceLocation CRACK_TEXTURE = new ResourceLocation(MinsAnime.MODID, "textures/effect/ice_crack.png");
	public static final ResourceLocation ICE_TEXTURE = new ResourceLocation("textures/block/ice.png");
	
	public IceRenderer(Context p_174008_)
	{
		super(p_174008_);
	}
	
	@Override
	public void render(EntityIce p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) 
	{
		if(p_114485_.getIceYSize() > 0.0F)
		{
			p_114488_.pushPose();
			p_114488_.mulPose(Axis.XP.rotationDegrees(p_114485_.getXRot()));
			AnimeClientUtil.drawBox(p_114485_.getBoundingBox().move(p_114485_.position().reverse()), p_114488_, p_114489_.getBuffer(RenderType.entityTranslucent(ICE_TEXTURE)), new Vec3(1, 1, 1), LightTexture.FULL_BRIGHT, 1.0F);
			AnimeClientUtil.drawBox(p_114485_.getBoundingBox().move(p_114485_.position().reverse()), p_114488_, p_114489_.getBuffer(AnimeRenderType.additive(ICE_TEXTURE)), new Vec3(0.5F, 0.5F, 0.5F), LightTexture.FULL_BRIGHT, 1.0F);
			p_114488_.popPose();
			
			p_114488_.pushPose();
			p_114488_.translate(-0.5F, 0.01F, -0.5F);
			p_114488_.mulPose(Axis.XP.rotationDegrees(90.0F));
			AnimeClientUtil.drawQuad(p_114488_, p_114489_.getBuffer(RenderType.entityTranslucent(CRACK_TEXTURE)), p_114485_.getIceSize() * 2.0F, LightTexture.FULL_BRIGHT, 0.5F);
			AnimeClientUtil.drawQuad(p_114488_, p_114489_.getBuffer(AnimeRenderType.additive(CRACK_TEXTURE)), p_114485_.getIceSize() * 2.0F, LightTexture.FULL_BRIGHT, 0.5F);
			p_114488_.popPose();
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityIce p_114482_)
	{
		return null;
	}
}
