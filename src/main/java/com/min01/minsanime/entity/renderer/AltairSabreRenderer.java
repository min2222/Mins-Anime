package com.min01.minsanime.entity.renderer;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.model.ModelAltairSabre;
import com.min01.minsanime.entity.projectile.EntityAltairSabre;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class AltairSabreRenderer extends EntityRenderer<EntityAltairSabre>
{
    public final ModelAltairSabre model;
    
	public AltairSabreRenderer(Context p_174008_)
	{
		super(p_174008_);
		this.model = new ModelAltairSabre(p_174008_.bakeLayer(ModelAltairSabre.LAYER_LOCATION));
	}
	
	@Override
	public void render(EntityAltairSabre p_114485_, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) 
	{
		p_114488_.pushPose();
		p_114488_.scale(-1.0F, -1.0F, 1.0F);
		p_114488_.scale(0.8F, 0.8F, 0.8F);
		p_114488_.translate(0.0F, -0.3F, 0.0F);
		p_114488_.mulPose(Axis.YP.rotationDegrees(-Mth.lerp(p_114487_, p_114485_.yRotO, p_114485_.getYRot()) - 90.0F));
		p_114488_.mulPose(Axis.ZP.rotationDegrees(-Mth.lerp(p_114487_, p_114485_.xRotO, p_114485_.getXRot()) + 90.0F));
		this.model.renderToBuffer(p_114488_, p_114489_.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(p_114485_))), p_114490_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		p_114488_.popPose();
	}
	
	@Override
	public ResourceLocation getTextureLocation(EntityAltairSabre p_114482_)
	{
		return new ResourceLocation(MinsAnime.MODID, "textures/entity/altair_sabre.png");
	}
}
