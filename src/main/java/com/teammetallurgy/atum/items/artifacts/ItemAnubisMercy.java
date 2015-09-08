package com.teammetallurgy.atum.items.artifacts;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemAnubisMercy extends Item {

    public ItemAnubisMercy() {
        super();
        this.setMaxDamage(20);
    }

    public static ChunkCoordinates verifyRespawnCoordinates(World par0World, ChunkCoordinates par1ChunkCoordinates, boolean par2) {
        if (!par0World.isRemote) {
            IChunkProvider c = par0World.getChunkProvider();
            c.loadChunk(par1ChunkCoordinates.posX - 3 >> 4, par1ChunkCoordinates.posZ - 3 >> 4);
            c.loadChunk(par1ChunkCoordinates.posX + 3 >> 4, par1ChunkCoordinates.posZ - 3 >> 4);
            c.loadChunk(par1ChunkCoordinates.posX - 3 >> 4, par1ChunkCoordinates.posZ + 3 >> 4);
            c.loadChunk(par1ChunkCoordinates.posX + 3 >> 4, par1ChunkCoordinates.posZ + 3 >> 4);
        }

        Block block = par0World.getBlock(par1ChunkCoordinates.posX, par1ChunkCoordinates.posY, par1ChunkCoordinates.posZ);
        if (block != null && block.isBed(par0World, par1ChunkCoordinates.posX, par1ChunkCoordinates.posY, par1ChunkCoordinates.posZ, (EntityLiving) null)) {
            ChunkCoordinates material1 = block.getBedSpawnPosition(par0World, par1ChunkCoordinates.posX, par1ChunkCoordinates.posY, par1ChunkCoordinates.posZ, (EntityPlayer) null);
            return material1;
        } else {
            Material material = par0World.getBlock(par1ChunkCoordinates.posX, par1ChunkCoordinates.posY, par1ChunkCoordinates.posZ).getMaterial();
            Material material1 = par0World.getBlock(par1ChunkCoordinates.posX, par1ChunkCoordinates.posY + 1, par1ChunkCoordinates.posZ).getMaterial();
            boolean flag1 = !material.isSolid() && !material.isLiquid();
            boolean flag2 = !material1.isSolid() && !material1.isLiquid();
            return par2 && flag1 && flag2 ? par1ChunkCoordinates : null;
        }
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return true;
    }

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            ItemStack stack = null;
            ItemStack[] damageAmount = player.inventory.mainInventory;
            int resistance = damageAmount.length;

            for (int i = 0; i < resistance; ++i) {
                ItemStack currStack = damageAmount[i];
                if (currStack != null && currStack.getItem() == this) {
                    stack = currStack;
                    break;
                }
            }

            if (stack == null) {
                return;
            }

            float var8 = event.ammount;
            if (!event.source.isUnblockable()) {
                var8 = (event.ammount * (25 - player.getTotalArmorValue()) + player.getAbsorptionAmount()) / 25.0F;
            }

            if (player.isPotionActive(Potion.resistance)) {
                resistance = 25 - (player.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
                var8 = var8 * (float) resistance / 25.0F;
            }

            if (Math.ceil((double) var8) >= (double) player.getHealth()) {
                event.setCanceled(true);
                this.respawnPlayer(event.entityLiving.worldObj, player);
                player.setHealth(player.getMaxHealth());
                player.getFoodStats().setFoodLevel(20);
                player.getFoodStats().setFoodSaturationLevel(20.0F);
                // player.spawnExplosionParticle();
                stack.damageItem(1, player);
                if (stack.getItemDamage() >= 20) {
                    stack = null;
                }
            }
        }

    }

    public void respawnPlayer(World par3World, EntityPlayer par2EntityPlayer) {
        ChunkCoordinates spawn = par2EntityPlayer.getBedLocation(par2EntityPlayer.dimension);
        if (spawn == null) {
            spawn = par3World.getSpawnPoint();
        }

        if (spawn == null) {
            spawn = par3World.getSpawnPoint();
        }

        spawn = verifyRespawnCoordinates(par3World, spawn, false);
        if (spawn == null) {
            spawn = par3World.getSpawnPoint();
        }

        par2EntityPlayer.rotationPitch = 0.0F;
        par2EntityPlayer.rotationYaw = 0.0F;
        par2EntityPlayer.setPositionAndUpdate((double) spawn.posX + 0.5D, (double) spawn.posY + 0.1D, (double) spawn.posZ);

        while (!par3World.getCollidingBoundingBoxes(par2EntityPlayer, par2EntityPlayer.boundingBox).isEmpty()) {
            par2EntityPlayer.setPosition(par2EntityPlayer.posX, par2EntityPlayer.posY + 1.0D, par2EntityPlayer.posZ);
        }

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
    public void registerIcons(IIconRegister par1IIconRegister) {
        this.itemIcon = par1IIconRegister.registerIcon("atum:AnubisMercy");
    }
}
