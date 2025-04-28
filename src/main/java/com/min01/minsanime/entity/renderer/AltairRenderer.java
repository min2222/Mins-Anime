package com.min01.minsanime.entity.renderer;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityAltair;
import com.min01.minsanime.entity.model.ModelAltair;
import com.min01.minsanime.entity.model.ModelAltairSabre;
import com.min01.minsanime.misc.AnimeRenderType;
import com.min01.minsanime.misc.HolopsiconAnimation;
import com.min01.minsanime.obj.ObjModelManager;
import com.min01.minsanime.obj.WavefrontObject;
import com.min01.minsanime.util.AnimeClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class AltairRenderer extends MobRenderer<EntityAltair, ModelAltair>
{
    public static final WavefrontObject HOLOPSICON = ObjModelManager.getInstance().getModel(new ResourceLocation(MinsAnime.MODID, "models/effect/holopsicon.obj"));
    public static final ResourceLocation TEXTURE = new ResourceLocation(MinsAnime.MODID, "textures/effect/holopsicon.png");
    public final ModelAltairSabre sabreModel;
	
	public AltairRenderer(Context p_174304_) 
	{
		super(p_174304_, new ModelAltair(p_174304_.bakeLayer(ModelAltair.LAYER_LOCATION)), 0.5F);
		this.sabreModel = new ModelAltairSabre(p_174304_.bakeLayer(ModelAltairSabre.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityAltair p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) 
	{
		super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
		
		float age = p_115455_.tickCount + p_115457_;
		float rotateAngleY = age * 10;

		int count = p_115455_.getSabreCount();
		for(int i = 0; i < count; i++) 
		{
			p_115458_.pushPose();
			p_115458_.scale(-1.0F, -1.0F, 1.0F);
			p_115458_.mulPose(Axis.YP.rotationDegrees(rotateAngleY * (180.0F / (float) Math.PI) + (i * (360.0F / count))));
			p_115458_.translate(-1.2F, -1.0F, -1.2F);
			p_115458_.scale(0.5F, 0.5F, 0.5F);
			if(p_115455_.isAggressive())
			{
				p_115458_.mulPose(Axis.XP.rotationDegrees(-90.0F));
				p_115458_.mulPose(Axis.ZP.rotationDegrees(180.0F + 45.0F));
			}
			else
			{
				p_115458_.mulPose(Axis.YP.rotationDegrees(45.0F));
				p_115458_.mulPose(Axis.ZP.rotationDegrees(180.0F));
			}
			
			this.sabreModel.renderToBuffer(p_115458_, p_115459_.getBuffer(RenderType.entityTranslucent(new ResourceLocation(MinsAnime.MODID, "textures/entity/altair_sabre.png"))), p_115460_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			p_115458_.popPose();
		}

		if(p_115455_.isHolopsicon())
		{
			this.renderHolopsicon(p_115455_, p_115458_, p_115459_, p_115460_, p_115457_, false);
			this.renderHolopsicon(p_115455_, p_115458_, p_115459_, p_115460_, p_115457_, true);
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityAltair p_115812_) 
	{
		return new ResourceLocation(MinsAnime.MODID, "textures/entity/altair.png");
	}
	
	public void renderHolopsicon(EntityAltair entity, PoseStack stack, MultiBufferSource source, int light, float partialTicks, boolean outer)
	{
		stack.pushPose();
		stack.scale(0.5F / 16.0F, 0.5F / 16.0F, 0.5F / 16.0F);
		AnimeClientUtil.resetPose(HOLOPSICON);
		if(outer)
		{
			AnimeClientUtil.animateObj(HOLOPSICON, entity.holopsiconOBJAnimationState, HolopsiconAnimation.SPELL, entity.tickCount + partialTicks);
		}
		else
		{
			AnimeClientUtil.animateObj(HOLOPSICON, entity.holopsiconOBJAnimationState, HolopsiconAnimation.DEATH, entity.tickCount + partialTicks);
		}
		AnimeClientUtil.animateObj(HOLOPSICON, entity.holopsiconUVAnimationState, HolopsiconAnimation.UV_ANIM, entity.tickCount + partialTicks);
		AnimeClientUtil.renderObj(stack, source, HOLOPSICON, AnimeRenderType.holopsicon(TEXTURE), light);
		stack.popPose();
	}
}
