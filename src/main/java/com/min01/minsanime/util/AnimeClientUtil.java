package com.min01.minsanime.util;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.min01.minsanime.obj.Face;
import com.min01.minsanime.obj.ObjAnimationDefinition;
import com.min01.minsanime.obj.ObjAnimations;
import com.min01.minsanime.obj.WavefrontObject;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.AnimationState;

public class AnimeClientUtil 
{
	public static final Minecraft MC = Minecraft.getInstance();
	
	public static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
	
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
}
