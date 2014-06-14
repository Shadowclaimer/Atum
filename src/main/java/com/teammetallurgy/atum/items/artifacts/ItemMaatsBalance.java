package com.teammetallurgy.atum.items.artifacts;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import org.lwjgl.input.Keyboard;

import com.teammetallurgy.atum.items.ItemTexturedArmor;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemMaatsBalance extends ItemTexturedArmor {

	public ItemMaatsBalance(ArmorMaterial par2ArmorMaterial, int par3, int par4) {
		super(par2ArmorMaterial, par3, par4);
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack, int pass) {
		return true;
	}

	@SubscribeEvent
	public void onLivingAttack(LivingHurtEvent event) {
		if (event.entityLiving.getEquipmentInSlot(3) != null && event.entityLiving.getEquipmentInSlot(3).getItem() == this) {
			event.ammount = (int) ((float) (event.ammount + 1) / 1.5F) - 1;
		}

		if (event.source instanceof EntityDamageSource) {
			EntityDamageSource source = (EntityDamageSource) event.source;
			if (source.getEntity() != null && source.getEntity() instanceof EntityLiving) {
				EntityLiving entity = (EntityLiving) source.getEntity();
				if (entity.getEquipmentInSlot(3) != null && entity.getEquipmentInSlot(3).getItem() == this) {
					event.ammount = (int) ((float) (event.ammount + 1) / 1.5F) - 1;
				}
			}
		}

	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.rare;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (Keyboard.isKeyDown(42)) {
			par3List.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal(this.getUnlocalizedName() + ".line1"));
			par3List.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal(this.getUnlocalizedName() + ".line2"));
		} else {
			par3List.add(StatCollector.translateToLocal(this.getUnlocalizedName() + ".line3") + " " + EnumChatFormatting.DARK_GRAY + "[SHIFT]");
		}
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == Items.diamond;
	}

	@Override
	public void registerIcons(IIconRegister par1IIconRegister) {
		this.itemIcon = par1IIconRegister.registerIcon("atum:MaatsBalance");
	}
}
