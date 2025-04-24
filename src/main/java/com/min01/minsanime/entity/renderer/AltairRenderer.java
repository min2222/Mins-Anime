package com.min01.minsanime.entity.renderer;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityAltair;
import com.min01.minsanime.entity.model.ModelAltair;
import com.min01.minsanime.entity.model.ModelAltairSabre;
import com.min01.minsanime.obj.ObjModelManager;
import com.min01.minsanime.obj.WavefrontObject;
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
    public static final WavefrontObject HOLOPSICON = ObjModelManager.getInstance().getModel(new ResourceLocation(MinsAnime.MODID, "models/entity/holopsicon.obj"));
    public static final WavefrontObject WIND = ObjModelManager.getInstance().getModel(new ResourceLocation(MinsAnime.MODID, "models/entity/wind.obj"));
    
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
				p_115458_.mulPose(Axis.ZP.rotationDegrees(180.0F));
			}
			
			this.sabreModel.renderToBuffer(p_115458_, p_115459_.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(p_115455_))), p_115460_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			p_115458_.popPose();
		}
	}

	@Override
	public ResourceLocation getTextureLocation(EntityAltair p_115812_) 
	{
		return new ResourceLocation(MinsAnime.MODID, "textures/entity/altair.png");
	}
}
