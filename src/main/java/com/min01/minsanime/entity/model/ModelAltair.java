package com.min01.minsanime.entity.model;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityAltair;
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

public class ModelAltair extends HierarchicalModel<EntityAltair>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MinsAnime.MODID, "altair"), "main");
	private final ModelPart root;
	private final ModelPart Altair;
	private final ModelPart head;
	private final ModelPart cutlass;
	private final ModelPart tommy_gun;

	public ModelAltair(ModelPart root) 
	{
		this.root = root.getChild("root");
		this.Altair = this.root.getChild("Altair");
		this.head = this.Altair.getChild("head");
		this.cutlass = this.Altair.getChild("cutlass");
		this.tommy_gun = this.Altair.getChild("tommy_gun");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Altair = root.addOrReplaceChild("Altair", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = Altair.addOrReplaceChild("head", CubeListBuilder.create().texOffs(68, 67).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition hair = head.addOrReplaceChild("hair", CubeListBuilder.create().texOffs(0, 71).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition front_hair = hair.addOrReplaceChild("front_hair", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 4.625F));

		PartDefinition front_strand_a = front_hair.addOrReplaceChild("front_strand_a", CubeListBuilder.create().texOffs(104, 91).addBox(-3.0F, 0.0F, 0.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -1.0F, -8.75F));

		front_strand_a.addOrReplaceChild("bone_a", CubeListBuilder.create().texOffs(44, 28).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 6.0F, 0.0F));

		PartDefinition front_strand_b = front_hair.addOrReplaceChild("front_strand_b", CubeListBuilder.create().texOffs(44, 34).addBox(-1.0F, 0.0F, 0.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -1.0F, -8.75F));

		front_strand_b.addOrReplaceChild("bone_b", CubeListBuilder.create().texOffs(118, 120).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 6.0F, 0.0F));

		PartDefinition middle_hair = hair.addOrReplaceChild("middle_hair", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition middle_pigtail_a = middle_hair.addOrReplaceChild("middle_pigtail_a", CubeListBuilder.create().texOffs(116, 71).addBox(0.0F, 0.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -8.0F, 0.0F, 0.3655F, -0.7119F, -0.5299F));

		PartDefinition middle_bone_a = middle_pigtail_a.addOrReplaceChild("middle_bone_a", CubeListBuilder.create().texOffs(0, 117).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		middle_bone_a.addOrReplaceChild("middle_bone_b", CubeListBuilder.create().texOffs(8, 117).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition middle_pigtail_b = middle_hair.addOrReplaceChild("middle_pigtail_b", CubeListBuilder.create().texOffs(24, 117).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -8.0F, 0.0F, 0.3655F, 0.7119F, 0.5299F));

		PartDefinition middle_bone_c = middle_pigtail_b.addOrReplaceChild("middle_bone_c", CubeListBuilder.create().texOffs(32, 117).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 8.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		middle_bone_c.addOrReplaceChild("middle_bone_d", CubeListBuilder.create().texOffs(16, 117).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition back_hair = hair.addOrReplaceChild("back_hair", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, -4.0F));

		PartDefinition back_pigtail_a = back_hair.addOrReplaceChild("back_pigtail_a", CubeListBuilder.create().texOffs(56, 71).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 8.25F, 0.1309F, 0.0F, 0.2618F));

		PartDefinition back_bone_a = back_pigtail_a.addOrReplaceChild("back_bone_a", CubeListBuilder.create().texOffs(44, 87).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		back_bone_a.addOrReplaceChild("back_bone_b", CubeListBuilder.create().texOffs(29, 4).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition back_pigtail_b = back_hair.addOrReplaceChild("back_pigtail_b", CubeListBuilder.create().texOffs(32, 109).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.0F, 8.25F, 0.1309F, 0.0F, -0.2618F));

		PartDefinition back_bone_c = back_pigtail_b.addOrReplaceChild("back_bone_c", CubeListBuilder.create().texOffs(94, 113).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		back_bone_c.addOrReplaceChild("back_bone_d", CubeListBuilder.create().texOffs(32, 101).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(92, 30).addBox(-4.5F, -14.0F, -4.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(84, 30).addBox(-4.0F, -11.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(84, 32).addBox(3.0F, -11.0F, -5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(104, 60).addBox(-4.0F, -6.0F, -7.5F, 8.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(44, 95).addBox(-1.0F, -8.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		hat.addOrReplaceChild("thingy_a", CubeListBuilder.create().texOffs(52, 95).addBox(-0.5F, 1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, -10.5F, -5.0F));

		hat.addOrReplaceChild("thingy_b", CubeListBuilder.create().texOffs(54, 95).addBox(-0.5F, 1.0F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -10.5F, -5.0F));

		PartDefinition body = Altair.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 71).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(56, 83).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(68, 54).addBox(-4.5F, -7.5F, -4.5F, 9.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, 0.0F));

		PartDefinition belt = body.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(84, 0).addBox(-4.5F, 4.0F, -2.5F, 9.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(84, 8).addBox(-4.5F, 4.0F, -2.5F, 9.0F, 3.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition skirt = belt.addOrReplaceChild("skirt", CubeListBuilder.create().texOffs(0, 54).addBox(-4.5F, 5.5F, -4.0F, 9.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(34, 54).addBox(-4.5F, 5.5F, -4.0F, 9.0F, 9.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		skirt.addOrReplaceChild("left_skirt_part_layer_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.25F))
		.texOffs(0, 87).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 6.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

		skirt.addOrReplaceChild("right_skirt_part_layer_r1", CubeListBuilder.create().texOffs(22, 87).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.25F))
		.texOffs(84, 16).addBox(-2.5F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition pouches = belt.addOrReplaceChild("pouches", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		pouches.addOrReplaceChild("pouch_r1", CubeListBuilder.create().texOffs(112, 102).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(112, 91).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(5.5F, 6.0F, 1.5F, 0.0F, 0.0F, -0.5236F));

		pouches.addOrReplaceChild("pouch_layer_r1", CubeListBuilder.create().texOffs(112, 0).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.25F))
		.texOffs(29, 13).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.5F, 6.0F, 1.5F, 0.0F, 0.0F, 0.5236F));

		Altair.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(44, 99).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(28, 27).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-4.0F, -22.0F, 0.0F));

		Altair.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(60, 99).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(16, 101).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
		.texOffs(104, 83).addBox(-0.5F, 0.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -22.0F, 0.0F));

		Altair.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(80, 97).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(100, 67).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

		Altair.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(96, 97).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 101).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(2.0F, -12.0F, 0.0F));

		PartDefinition cutlass = Altair.addOrReplaceChild("cutlass", CubeListBuilder.create().texOffs(52, 0).addBox(-10.0F, -35.0F, 0.5F, 16.0F, 42.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -13.0F, 0.0F, 1.5708F, 0.0F, 1.5708F));

		PartDefinition handle_for_cutlass = cutlass.addOrReplaceChild("handle_for_cutlass", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, 0.5F));

		handle_for_cutlass.addOrReplaceChild("rotate", CubeListBuilder.create().texOffs(104, 64).addBox(0.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(110, 64).addBox(1.0F, -3.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 79).addBox(2.0F, -4.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(120, 55).addBox(3.0F, -5.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.5F, 0.0F, 0.0F, 0.0F, -0.7854F));

		cutlass.addOrReplaceChild("handguard", CubeListBuilder.create().texOffs(112, 11).addBox(-4.0F, -8.5F, -0.5F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(56, 79).addBox(-3.0F, -0.5F, -0.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 4.0F, 0.0F));

		PartDefinition tommy_gun = Altair.addOrReplaceChild("tommy_gun", CubeListBuilder.create(), PartPose.offset(7.0F, -10.0F, -2.0F));

		PartDefinition handle_for_tommy_gun = tommy_gun.addOrReplaceChild("handle_for_tommy_gun", CubeListBuilder.create().texOffs(112, 7).addBox(0.0F, -8.0F, -24.0F, 0.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 13.0F));

		handle_for_tommy_gun.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(106, 113).addBox(-0.5F, -1.5F, -2.5F, 1.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.6307F, -12.8061F, -0.3927F, 0.0F, 0.0F));

		handle_for_tommy_gun.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(45, 115).addBox(0.0F, -3.0F, -1.5F, 1.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -4.25F, -18.5F, 0.7854F, 0.0F, 0.0F));

		handle_for_tommy_gun.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(80, 83).addBox(-0.5F, -2.0F, -5.0F, 2.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -4.3194F, -6.1225F, -0.0873F, 0.0F, 0.0F));

		PartDefinition barrel = tommy_gun.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -9.0F, -36.0F, 2.0F, 3.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(0, 27).addBox(-1.5F, -9.0F, -36.0F, 2.0F, 3.0F, 24.0F, new CubeDeformation(0.25F))
		.texOffs(118, 113).addBox(-0.5F, -12.0F, -36.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 4.5F, 13.0F));

		barrel.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(76, 113).addBox(-3.0F, -3.0F, -1.5F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -3.0F, -25.5F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityAltair entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		AnimeClientUtil.animateHead(this.head, netHeadYaw, headPitch);
		this.cutlass.visible = false;
		this.tommy_gun.visible = false;
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