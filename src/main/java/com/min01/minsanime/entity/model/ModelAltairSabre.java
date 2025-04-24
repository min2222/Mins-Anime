package com.min01.minsanime.entity.model;

import com.min01.minsanime.MinsAnime;
import com.min01.minsanime.entity.projectile.EntityAltairSabre;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class ModelAltairSabre extends EntityModel<EntityAltairSabre>
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(MinsAnime.MODID, "altairsabre"), "main");
	private final ModelPart root;

	public ModelAltairSabre(ModelPart root) 
	{
		this.root = root.getChild("root");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cutlass = root.addOrReplaceChild("cutlass", CubeListBuilder.create().texOffs(57, 0).addBox(-5.5F, -21.0F, 0.0F, 11.0F, 42.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -19.5F, 0.0F));

		PartDefinition handle_for_cutlass = cutlass.addOrReplaceChild("handle_for_cutlass", CubeListBuilder.create(), PartPose.offset(-0.5F, 14.5F, 0.0F));

		handle_for_cutlass.addOrReplaceChild("rotate", CubeListBuilder.create().texOffs(104, 64).addBox(0.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(110, 64).addBox(1.0F, -3.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(116, 79).addBox(2.0F, -4.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(120, 55).addBox(3.0F, -5.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.5F, 0.0F, 0.0F, 0.0F, -0.7854F));

		cutlass.addOrReplaceChild("handguard", CubeListBuilder.create().texOffs(112, 11).addBox(-4.0F, -8.5F, -0.5F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(56, 79).addBox(-3.0F, -0.5F, -0.5F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 18.0F, -0.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(EntityAltairSabre entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) 
	{
		
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}