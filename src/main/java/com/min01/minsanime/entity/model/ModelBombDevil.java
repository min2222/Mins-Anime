package com.min01.minsanime.entity.model;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.animation.BombDevilAnimation;
import com.min01.minsanime.entity.animation.RezeAnimation;
import com.min01.minsanime.entity.living.EntityReze;
import com.min01.minsanime.util.AnimeClientUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ModelBombDevil extends HierarchicalModel<EntityReze>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MinsAnime.MODID, "bomb_devil"), "main");
	private final ModelPart root;
	private final ModelPart bomb_devil;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart left_leg;
	private final ModelPart right_leg;

	public ModelBombDevil(ModelPart root)
	{
		this.root = root.getChild("root");
		this.bomb_devil = this.root.getChild("bomb_devil");
		this.body = this.bomb_devil.getChild("body");
		this.head = this.body.getChild("head");
		this.left_arm = this.body.getChild("left_arm");
		this.right_arm = this.body.getChild("right_arm");
		this.left_leg = this.bomb_devil.getChild("left_leg");
		this.right_leg = this.bomb_devil.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bomb_devil = root.addOrReplaceChild("bomb_devil", CubeListBuilder.create(), PartPose.offset(0.0F, -16.0F, 0.0F));

		PartDefinition body = bomb_devil.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 13).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		PartDefinition middle = body.addOrReplaceChild("middle", CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 73).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 8.0F, -1.5F));

		PartDefinition middle2 = middle.addOrReplaceChild("middle2", CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 73).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition middle3 = middle2.addOrReplaceChild("middle3", CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 73).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		middle3.addOrReplaceChild("middle4", CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 73).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 4.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-4.0F, -3.0F, -2.0F, 8.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(28, 29).addBox(-4.0F, -8.0F, -8.0F, 8.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 84).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 88).addBox(-8.5F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -1.0F, -1.25F, 0.7854F, 0.0F, 0.0F));

		PartDefinition down = head.addOrReplaceChild("down", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.0F, -1.0F, 0.3927F, 0.0F, 0.0F));

		down.addOrReplaceChild("rotate", CubeListBuilder.create().texOffs(16, 59).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(16, 67).addBox(-3.5F, -3.0F, -1.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(68, 16).addBox(-3.5F, -3.25F, -1.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition back = head.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 38).addBox(0.0F, -5.0F, -1.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 5.0F));

		back.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 38).addBox(0.0F, -5.0F, -4.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, 0.0F, -1.5708F));

		back.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 38).addBox(0.0F, -5.0F, -4.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(52, 34).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, 0.0F, -0.7854F));

		back.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 38).addBox(0.0F, -5.0F, -4.0F, 0.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.7854F));

		body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(64, 0).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(64, 50).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(64, 34).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(64, 66).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		bomb_devil.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(48, 41).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(52, 18).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(2.0F, 4.0F, 0.0F));

		bomb_devil.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 56).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 57).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-2.0F, 4.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityReze entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		AnimeClientUtil.animateHead(this.head, netHeadYaw, headPitch);
		
		entity.transformAnimationState.animate(this, RezeAnimation.REZE_TRANSFORM, ageInTicks);
		entity.shootAnimationState.animate(this, BombDevilAnimation.BOMB_DEVIL_SHOOT, ageInTicks);
		entity.catchAnimationState.animate(this, BombDevilAnimation.BOMB_DEVIL_CATCH, ageInTicks);
		entity.dashAnimationState.animate(this, BombDevilAnimation.BOMB_DEVIL_DASH, ageInTicks);
		entity.jumpAnimationState.animate(this, BombDevilAnimation.BOMB_DEVIL_JUMP, ageInTicks);
		entity.kickAnimationState.animate(this, BombDevilAnimation.BOMB_DEVIL_KICK, ageInTicks);
		
		if(entity.isUsingSkill(4))
		{
			AnimeClientUtil.animateHead(this.bomb_devil, netHeadYaw, headPitch);
		}
		
		if(!entity.isUsingSkill())
		{
			this.left_arm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
	        this.right_arm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
	        this.right_leg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
	        this.left_leg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
		}
	}
	
	@Override
	public ModelPart root() 
	{
		return this.root;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}