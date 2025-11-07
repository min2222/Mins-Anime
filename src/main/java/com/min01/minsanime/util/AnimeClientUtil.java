package com.min01.minsanime.util;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.ITrail;
import com.min01.minsanime.misc.AnimeRenderType;
import com.min01.minsanime.obj.Face;
import com.min01.minsanime.obj.ObjAnimationDefinition;
import com.min01.minsanime.obj.ObjAnimations;
import com.min01.minsanime.obj.WavefrontObject;
import com.min01.minsanime.shader.AnimeShaderEffects.ShaderEffect;
import com.min01.minsanime.shader.AnimeShaders;
import com.min01.minsanime.shader.ExtendedPostChain;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AnimeClientUtil 
{
	public static final Minecraft MC = Minecraft.getInstance();
	
	public static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
	public static final Matrix4f INVERSE_MAT = new Matrix4f();
	
    public static void applyZoltraak(PoseStack mtx, float frameTime, float tickCount, Vec3 endPos, float maxScale)
	{
		Minecraft minecraft = AnimeClientUtil.MC;

		ExtendedPostChain shaderChain = AnimeShaders.getZoltraak();
		EffectInstance shader = shaderChain.getMainShader();

		if(shader != null)
		{
			shader.safeGetUniform("InverseTransformMatrix").set(getInverseTransformMatrix(INVERSE_MAT, mtx.last().pose()));
			shader.safeGetUniform("iTime").set(tickCount / 20.0F);
			shader.safeGetUniform("EndPos").set((float) endPos.x, (float) endPos.y, (float) endPos.z);
			shader.safeGetUniform("MaxScale").set(maxScale);
			shaderChain.process(frameTime);
			minecraft.getMainRenderTarget().bindWrite(false);
		}
	}
	
    public static void applyLight(PoseStack mtx, float frameTime)
	{
		Minecraft minecraft = AnimeClientUtil.MC;

		ExtendedPostChain shaderChain = AnimeShaders.getLight();
		EffectInstance shader = shaderChain.getMainShader();

		if(shader != null)
		{
			shader.safeGetUniform("InverseTransformMatrix").set(getInverseTransformMatrix(INVERSE_MAT, mtx.last().pose()));
			shaderChain.process(frameTime);
			minecraft.getMainRenderTarget().bindWrite(false);
		}
	}
    
    public static void applyBullet(PoseStack mtx, float frameTime)
	{
		Minecraft minecraft = AnimeClientUtil.MC;

		ExtendedPostChain shaderChain = AnimeShaders.getBullet();
		EffectInstance shader = shaderChain.getMainShader();

		if(shader != null)
		{
			shader.safeGetUniform("iResolution").set(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
			shader.setSampler("ImageSampler", () -> minecraft.getTextureManager().getTexture(new ResourceLocation(MinsAnime.MODID, "textures/misc/rgba_noise_medium.png")).getId());
			shader.safeGetUniform("InverseTransformMatrix").set(getInverseTransformMatrix(INVERSE_MAT, mtx.last().pose()));
			shader.safeGetUniform("iTime").set((((float) (minecraft.level.getGameTime() % 2400000)) + frameTime) / 20.0F);
			shaderChain.process(frameTime);
			minecraft.getMainRenderTarget().bindWrite(false);
		}
	}
    
	public static void applyColoredExplosion(PoseStack mtx, float frameTime, ShaderEffect effect)
	{
		Minecraft minecraft = AnimeClientUtil.MC;

		ExtendedPostChain shaderChain = AnimeShaders.getColoredExplosion();
		EffectInstance shader = shaderChain.getMainShader();

		if(shader != null)
		{
			shader.safeGetUniform("iResolution").set(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
			shader.setSampler("ImageSampler", effect::getOrCreateVolumeTextureId);
			shader.safeGetUniform("InverseTransformMatrix").set(getInverseTransformMatrix(INVERSE_MAT, mtx.last().pose()));
			shader.safeGetUniform("iTime").set(effect.tickCount / 20.0F);
			shader.safeGetUniform("Scale").set(effect.scale);
			shader.safeGetUniform("Color").set((float) effect.color.x, (float) effect.color.y, (float) effect.color.z);
			shaderChain.process(frameTime);
			minecraft.getMainRenderTarget().bindWrite(false);
		}
	}
	
	public static void applyExplosion(PoseStack mtx, float frameTime, float tickCount, float scale)
	{
		Minecraft minecraft = AnimeClientUtil.MC;

		ExtendedPostChain shaderChain = AnimeShaders.getExplosion();
		EffectInstance shader = shaderChain.getMainShader();

		if(shader != null)
		{
			shader.safeGetUniform("iResolution").set(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
			shader.setSampler("iChannel0", () -> minecraft.getTextureManager().getTexture(new ResourceLocation(MinsAnime.MODID, "textures/misc/organic4.png")).getId());
			shader.setSampler("iChannel1", () -> minecraft.getTextureManager().getTexture(new ResourceLocation(MinsAnime.MODID, "textures/misc/rgba_noise_medium.png")).getId());
			shader.safeGetUniform("InverseTransformMatrix").set(getInverseTransformMatrix(INVERSE_MAT, mtx.last().pose()));
			shader.safeGetUniform("iTime").set(tickCount / 20.0F);
			shader.safeGetUniform("Scale").set(scale);
			shaderChain.process(frameTime);
			minecraft.getMainRenderTarget().bindWrite(false);
		}
	}
	
	public static Matrix4f getInverseTransformMatrix(Matrix4f outMat, Matrix4f modelView)
    {
		return outMat.identity().mul(RenderSystem.getProjectionMatrix()).mul(modelView).invert();
    }
	
	public static void resetPose(WavefrontObject obj)
	{
		obj.x = 0.0F;
		obj.y = 0.0F;
		obj.z = 0.0F;
		obj.xRot = 0.0F;
		obj.yRot = 0.0F;
		obj.zRot = 0.0F;
		obj.xScale = 1.0F;
		obj.yScale = 1.0F;
		obj.zScale = 1.0F;
		obj.xUV = 0.0F;
		obj.yUV = 0.0F;
	}

	public static void animateObj(WavefrontObject obj, AnimationState state, ObjAnimationDefinition definition, float ageInTicks) 
	{
		state.updateTime(ageInTicks, 1.0F);
		state.ifStarted((anim) -> 
		{
			ObjAnimations.animate(obj, definition, anim.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
		});
	}
	
	public static void renderObj(PoseStack stack, MultiBufferSource source, WavefrontObject obj, RenderType renderType, int light)
	{
		stack.pushPose();
		Face.setUvOperator(1, 1, obj.xUV, obj.yUV);
		Face.setMatrix(stack);
		Face.setLightMap(light);
		stack.translate(obj.x / 16.0F, obj.y / 16.0F, obj.z / 16.0F);
		if(obj.xRot != 0.0F || obj.yRot != 0.0F || obj.zRot != 0.0F) 
		{
			stack.mulPose((new Quaternionf()).rotationZYX(obj.zRot, obj.yRot, obj.xRot));
		}
		if(obj.xScale != 1.0F || obj.yScale != 1.0F || obj.zScale != 1.0F)
		{
			stack.scale(obj.xScale, obj.yScale, obj.zScale);
		}
		obj.tessellateAll(source.getBuffer(renderType));
		Face.resetUvOperator();
		Face.resetMatrix();
		Face.resetLightMap();
		stack.popPose();
	}
	
	public static void animateHead(ModelPart head, float netHeadYaw, float headPitch)
	{
		head.yRot += Math.toRadians(netHeadYaw);
		head.xRot += Math.toRadians(headPitch);
	}
    
    public static void drawQuad(PoseStack stack, VertexConsumer consumer, float size, int packedLightIn, float alpha) 
    {
        float minU = 0;
        float minV = 0;
        float maxU = 1;
        float maxV = 1;
        PoseStack.Pose matrixstack$entry = stack.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        drawVertex(matrix4f, matrix3f, consumer, size, size, 0, minU, minV, alpha, packedLightIn);
        drawVertex(matrix4f, matrix3f, consumer, size, -size, 0, minU, maxV, alpha, packedLightIn);
        drawVertex(matrix4f, matrix3f, consumer, -size, -size, 0, maxU, maxV, alpha, packedLightIn);
        drawVertex(matrix4f, matrix3f, consumer, -size, size, 0, maxU, minV, alpha, packedLightIn);
    }
    
    public static void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, float offsetX, float offsetY, float offsetZ, float textureX, float textureY, float alpha, int packedLightIn)
    {
    	vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(1, 1, 1, 1 * alpha).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, 0.0F, 1.0F, 0.0F).endVertex();
    }
    
    public static void drawBox(AABB boundingBox, PoseStack stack, VertexConsumer consumer, Vec3 rgb, int light, float alpha) 
    {
        Matrix4f matrix4f = stack.last().pose();
        float maxX = (float) boundingBox.maxX * 0.625F;
        float minX = (float) boundingBox.minX * 0.625F;
        float maxY = (float) boundingBox.maxY * 0.625F;
        float minY = (float) boundingBox.minY * 0.625F;
        float maxZ = (float) boundingBox.maxZ * 0.625F;
        float minZ = (float) boundingBox.minZ * 0.625F;

        float maxU = maxZ - minZ;
        float maxV = maxY - minY;
        float minU = minZ - maxZ;
        float minV = minY - maxY;
        // X+
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1.0F, 0.0F, 0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1.0F, 0.0F, 0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1.0F, 0.0F, 0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(1.0F, 0.0F, 0F).endVertex();

        // X-
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(-1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(-1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(-1.0F, 0.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(-1.0F, 0.0F, 0.0F).endVertex();


        maxU = maxX - minX;
        maxV = maxY - minY;
        minU = minX - maxX;
        minV = minY - maxY;
        // Z-
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, -1.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, -1.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, -1.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, -1.0F).endVertex();

        // Z+
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 0.0F, 1.0F).endVertex();


        maxU = maxZ - minZ;
        maxV = maxX - minX;
        minU = minZ - maxZ;
        minV = minX - maxX;
        // Y+
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        // Y-
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, -1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, minV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, -1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(maxU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, -1.0F, 0.0F).endVertex();
        consumer.vertex(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).color((float)rgb.x, (float)rgb.y, (float)rgb.z, alpha).uv(minU, maxV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, -1.0F, 0.0F).endVertex();
    }
    
    public static void renderTrail(ITrail entityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, float trailR, float trailG, float trailB, float trailA, int sampleSize, float trailHeight) 
    {
    	Camera camera = MC.gameRenderer.getMainCamera();
    	Vec3 cameraPos = camera.getPosition();
        int samples = 0;
        Vec3 drawFrom = entityIn.getTrailPosition(0, partialTicks);
        VertexConsumer vertexconsumer = bufferIn.getBuffer(AnimeRenderType.eyesFix(new ResourceLocation(MinsAnime.MODID, "textures/effect/trail.png")));
        while(samples < sampleSize)
        {
            Vec3 sample = entityIn.getTrailPosition(samples, partialTicks);
            float u1 = samples / (float) sampleSize;
            float u2 = u1 + 1 / (float) sampleSize;
            Vec3 draw1 = drawFrom;
            Vec3 draw2 = sample;
            Vec3 segmentDir = draw2.subtract(draw1).normalize();
            Vec3 midPoint = draw1.add(draw2).scale(0.5);
            Vec3 toCamera = cameraPos.subtract(midPoint).normalize();
            Vec3 perpendicular = segmentDir.cross(toCamera).normalize().scale(trailHeight);
            Vec3 v1Bottom = draw1.subtract(perpendicular);
            Vec3 v1Top = draw1.add(perpendicular);
            Vec3 v2Bottom = draw2.subtract(perpendicular);
            Vec3 v2Top = draw2.add(perpendicular);
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix4f = pose.pose();
            Matrix3f matrix3f = pose.normal();
            vertexconsumer.vertex(matrix4f, (float) v1Bottom.x, (float) v1Bottom.y, (float) v1Bottom.z).color(trailR, trailG, trailB, trailA).uv(u1, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BLOCK).normal(matrix3f, 0, 1, 0).endVertex();
            vertexconsumer.vertex(matrix4f, (float) v2Bottom.x, (float) v2Bottom.y, (float) v2Bottom.z).color(trailR, trailG, trailB, trailA).uv(u2, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BLOCK).normal(matrix3f, 0, 1, 0).endVertex();
            vertexconsumer.vertex(matrix4f, (float) v2Top.x, (float) v2Top.y, (float) v2Top.z).color(trailR, trailG, trailB, trailA).uv(u2, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BLOCK).normal(matrix3f, 0, 1, 0).endVertex();
            vertexconsumer.vertex(matrix4f, (float) v1Top.x, (float) v1Top.y, (float) v1Top.z).color(trailR, trailG, trailB, trailA).uv(u1, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(LightTexture.FULL_BLOCK).normal(matrix3f, 0, 1, 0).endVertex();
            samples++;
            drawFrom = sample;
        }
    }
}
