package com.min01.minsanime.entity.renderer;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityFrieren;
import com.min01.minsanime.entity.model.ModelFrieren;
import com.min01.minsanime.misc.AnimeRenderType;
import com.min01.minsanime.shader.AnimeShaders;
import com.min01.minsanime.shader.ShaderEffectHandler;
import com.min01.minsanime.util.AnimeClientUtil;
import com.min01.minsanime.util.AnimeUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
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
		
		ShaderEffectHandler.renderEffect(AnimeShaders.BLUR);
		
		p_115458_.pushPose();
		float size = 0.5F;
		float xRot = Mth.rotLerp(p_115457_, p_115455_.xRotO, p_115455_.getXRot());
		float yRot = Mth.rotLerp(p_115457_, p_115455_.yHeadRotO, p_115455_.getYHeadRot());
		Vec3 pos = AnimeUtil.getLookPos(new Vec2(xRot, yRot), Vec3.ZERO, 0, 0, 1.5F);
		p_115458_.translate(pos.x, pos.y + 1, pos.z);
		p_115458_.mulPose(Axis.YP.rotationDegrees(-yRot + 180.0F));
		AnimeClientUtil.drawBox(new AABB(-size, -size, -100.0F, size, size, 0.0F), p_115458_, p_115459_, Vec3.fromRGB24(15659769), LightTexture.FULL_BRIGHT, 1, AnimeRenderType.blur(new ResourceLocation(MinsAnime.MODID, "textures/effect/white.png")));
		p_115458_.mulPose(Axis.ZP.rotationDegrees(p_115455_.tickCount / 2));
		AnimeClientUtil.renderFlatQuad(p_115458_, p_115459_.getBuffer(AnimeRenderType.eyesNoAlpha(new ResourceLocation(MinsAnime.MODID, "textures/effect/zoltraak.png"))), 1.0F, LightTexture.FULL_BRIGHT);
		p_115458_.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(EntityFrieren p_115812_)
	{
		return new ResourceLocation(MinsAnime.MODID, "textures/entity/frieren.png");
	}
}
