package com.min01.minsanime.entity.model;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.living.EntityFrieren;
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

public class ModelFrieren extends HierarchicalModel<EntityFrieren>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MinsAnime.MODID, "frieren"), "main");
	private final ModelPart root;
	private final ModelPart frieren_sama;
	private final ModelPart head;
	private final ModelPart rightarm;
	private final ModelPart staff;
	private final ModelPart leftarm;
	private final ModelPart rightleg;
	private final ModelPart leftleg;

	public ModelFrieren(ModelPart root) 
	{
		this.root = root.getChild("root");
		this.frieren_sama = this.root.getChild("frieren_sama");
		this.head = this.frieren_sama.getChild("head");
		this.rightarm = this.frieren_sama.getChild("rightarm");
		this.staff = this.rightarm.getChild("staff");
		this.leftarm = this.frieren_sama.getChild("leftarm");
		this.rightleg = this.frieren_sama.getChild("rightleg");
		this.leftleg = this.frieren_sama.getChild("leftleg");
	}

	public static LayerDefinition createBodyLayer() 
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frieren_sama = root.addOrReplaceChild("frieren_sama", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = frieren_sama.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -24.0F, 0.0F));

		PartDefinition elf_ears = head.addOrReplaceChild("elf_ears", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		elf_ears.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(18, 92).addBox(0.0F, -1.5F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(80, 90).addBox(0.0F, -1.5F, 2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -0.5F, 0.0F, 0.0F, 0.3927F, 0.0F));

		elf_ears.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(24, 92).addBox(-0.75F, -1.5F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(88, 91).addBox(-0.75F, -1.5F, 2.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.25F, -0.5F, 0.0F, 0.0F, -0.3927F, 0.0F));

		elf_ears.addOrReplaceChild("earrings", CubeListBuilder.create().texOffs(24, 50).addBox(5.0F, 23.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 53).addBox(-6.0F, 23.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -22.5F, 2.5F));

		PartDefinition armorHead = head.addOrReplaceChild("armorHead", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hair = armorHead.addOrReplaceChild("hair", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		hair.addOrReplaceChild("left_hair", CubeListBuilder.create().texOffs(32, 0).addBox(0.0F, -4.0F, 0.0F, 0.0F, 20.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -7.0F, 2.0F, 0.0873F, 1.1781F, 0.0F));

		hair.addOrReplaceChild("right_hair", CubeListBuilder.create().texOffs(28, 32).addBox(0.0F, -4.0F, 0.0F, 0.0F, 20.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -7.0F, 2.0F, 0.0873F, -1.1781F, 0.0F));

		PartDefinition body = frieren_sama.addOrReplaceChild("body", CubeListBuilder.create().texOffs(44, 42).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(48, 0).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -18.0F, 0.0F));

		PartDefinition armorBody = body.addOrReplaceChild("armorBody", CubeListBuilder.create().texOffs(0, 32).addBox(-4.5F, -0.5F, -2.5F, 9.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		armorBody.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(44, 58).addBox(-3.0F, -3.5F, -2.5F, 6.0F, 8.0F, 6.0F, new CubeDeformation(-0.025F)), PartPose.offsetAndRotation(1.75F, 12.0F, -0.5F, 0.0F, 0.0F, -0.0436F));

		armorBody.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 50).addBox(-3.0F, -3.5F, -2.5F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.75F, 12.0F, -0.5F, 0.0F, 0.0F, 0.0436F));

		PartDefinition rightarm = frieren_sama.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(24, 60).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 64).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-4.0F, -22.0F, 0.0F));

		rightarm.addOrReplaceChild("armorRightArm", CubeListBuilder.create().texOffs(84, 57).addBox(-4.5F, 6.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(16, 76).addBox(-4.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(80, 27).addBox(-4.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition staff = rightarm.addOrReplaceChild("staff", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.5F, 9.0F, 13.0F, 1.5708F, 0.0F, -1.5708F));

		PartDefinition handle = staff.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 80).addBox(-1.5F, 3.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(88, 86).addBox(-1.5F, -22.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(52, 90).addBox(-1.5F, -28.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		handle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(88, 18).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(72, 90).addBox(-1.0F, 3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(64, 90).addBox(-1.0F, 28.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -29.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		handle.addOrReplaceChild("cubes", CubeListBuilder.create().texOffs(32, 28).addBox(0.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(38, 28).addBox(1.0F, -3.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 92).addBox(2.0F, -4.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 95).addBox(3.0F, -5.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 95).addBox(4.0F, -6.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 95).addBox(5.0F, -7.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 95).addBox(6.0F, -8.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(36, 95).addBox(7.0F, -9.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 95).addBox(8.0F, -10.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 95).addBox(9.0F, -11.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(54, 95).addBox(10.0F, -12.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(80, 95).addBox(11.0F, -13.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 96).addBox(12.0F, -14.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(66, 96).addBox(13.0F, -15.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition headpart = staff.addOrReplaceChild("headpart", CubeListBuilder.create().texOffs(0, 86).addBox(-3.5F, -4.5F, 0.0F, 9.0F, 9.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(44, 28).addBox(-6.0F, 4.0F, -2.0F, 14.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 35).addBox(-6.0F, -7.0F, -2.0F, 14.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(88, 6).addBox(-6.0F, -4.0F, -2.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -38.0F, 0.0F));

		headpart.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(88, 80).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -8.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		headpart.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(88, 73).addBox(-1.5F, -1.5F, -1.0F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 88).addBox(-1.0F, -1.0F, -1.5F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -1.5F, 0.0F, 0.0F, 0.0F, 0.7854F));

		headpart.addOrReplaceChild("ribbon", CubeListBuilder.create().texOffs(16, 64).addBox(0.0F, 1.0F, -4.0F, 4.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 4.0F, 2.0F, -0.0436F, 0.0F, 0.0F));

		PartDefinition leftarm = frieren_sama.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(68, 42).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(68, 58).addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(4.0F, -22.0F, 0.0F));

		leftarm.addOrReplaceChild("armorLeftArm", CubeListBuilder.create().texOffs(84, 65).addBox(-12.5F, 6.0F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(84, 37).addBox(-12.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(84, 47).addBox(-12.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(12.0F, 0.0F, 0.0F));

		PartDefinition rightleg = frieren_sama.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(40, 72).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-2.0F, -12.0F, 0.0F));

		rightleg.addOrReplaceChild("armorRightBoot", CubeListBuilder.create().texOffs(88, 0).addBox(1.5F, 6.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.25F))
		.texOffs(68, 16).addBox(1.5F, 6.5F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 0.0F));

		PartDefinition leftleg = frieren_sama.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(72, 74).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(2.0F, -12.0F, 0.0F));

		leftleg.addOrReplaceChild("armorLeftBoot", CubeListBuilder.create().texOffs(48, 16).addBox(-2.5F, 6.5F, -2.5F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(18, 86).addBox(-2.5F, 6.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityFrieren entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.staff.visible = false;
		this.leftarm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.rightarm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
		this.rightleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.leftleg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
		AnimeClientUtil.animateHead(this.head, netHeadYaw, headPitch);
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