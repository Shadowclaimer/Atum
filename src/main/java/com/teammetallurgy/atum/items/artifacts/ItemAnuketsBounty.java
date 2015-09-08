package com.teammetallurgy.atum.items.artifacts;

import com.teammetallurgy.atum.entity.arrow.EntityAtumFishHook;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemAnuketsBounty extends Item {

    @SideOnly(Side.CLIENT)
    private IIcon theIcon;

    public ItemAnuketsBounty() {
        super();
        this.setMaxDamage(64);
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRotateAroundWhenRendering() {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par3EntityPlayer.fishEntity != null) {
            int i = par3EntityPlayer.fishEntity.func_146034_e();
            par1ItemStack.damageItem(i, par3EntityPlayer);
            par3EntityPlayer.swingItem();
        } else {
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
            if (!par2World.isRemote) {
                par2World.spawnEntityInWorld(new EntityAtumFishHook(par2World, par3EntityPlayer));
            }
            par3EntityPlayer.swingItem();
        }

        return par1ItemStack;
    }

    @Override
    public void registerIcons(IIconRegister par1IIconRegister) {
        this.itemIcon = par1IIconRegister.registerIcon("atum:AnuketsBounty");
        this.theIcon = par1IIconRegister.registerIcon("fishing_rod_cast");
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

        int remaining = par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage();
        String localizedRemaining = StatCollector.translateToLocalFormatted("tooltip.atum.usesRemaining", remaining);
        par3List.add(localizedRemaining);
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return par2ItemStack.getItem() == Items.diamond;
    }
}
