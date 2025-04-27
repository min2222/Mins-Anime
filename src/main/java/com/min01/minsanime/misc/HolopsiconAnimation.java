package com.min01.minsanime.misc;

import org.joml.Vector3f;

import com.min01.minsanime.obj.ObjAnimationChannel;
import com.min01.minsanime.obj.ObjAnimationDefinition;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class HolopsiconAnimation
{
	public static final ObjAnimationDefinition SPELL = ObjAnimationDefinition.Builder.withLength(0.5F)
			.addAnimation("bone_root", new ObjAnimationChannel(ObjAnimationChannel.Targets.SCALE, 
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.5F, KeyframeAnimations.scaleVec(10.0F, 10.0F, 10.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.build();
	
	public static final ObjAnimationDefinition DEATH = ObjAnimationDefinition.Builder.withLength(0.9F)
			.addAnimation("bone_root", new ObjAnimationChannel(ObjAnimationChannel.Targets.SCALE, 
					new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.5F, 1.5F, 1.5F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.672F, KeyframeAnimations.scaleVec(1.5F, 1.5F, 1.5F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.9F, KeyframeAnimations.scaleVec(5.0F, 5.0F, 5.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.build();
	
	public static final ObjAnimationDefinition UV_ANIM = ObjAnimationDefinition.Builder.withLength(1.5F).looping()
			.addAnimation("bone_root", new ObjAnimationChannel(ObjAnimationChannel.Targets.UV, 
					new Keyframe(0.0F, new Vector3f(0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(0.5F, new Vector3f(-0.5F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(1.0F, new Vector3f(0.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
					new Keyframe(1.5F, new Vector3f(-0.5F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
			))
			.build();
}
