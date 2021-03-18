package morph.common.ability;

import ichun.common.core.util.ObfHelper;
import morph.api.Ability;
import morph.common.Morph;
import morph.common.morph.MorphInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class AbilityFireImmunity extends Ability {

	@Override
	public String getType() 
	{
		return "fireImmunity";
	}

	@Override
	public void tick() 
	{
		MorphInfo info = null;
		if(getParent() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer)getParent();
			if(!player.worldObj.isRemote)
			{
				info = Morph.proxy.tickHandlerServer.getPlayerMorphInfo(player);
			}
			else
			{
				info = Morph.proxy.tickHandlerClient.playerMorphInfo.get(player.getCommandSenderName());
			}
		}
		
		boolean fireproof = true;
		
		if(info != null && info.nextState.entInstance instanceof EntitySkeleton)
		{
			EntitySkeleton skele = (EntitySkeleton)info.nextState.entInstance;
			if(skele.getSkeletonType() != 1)
			{
				fireproof = false;
			}
		}
		
		if(fireproof)
		{
			if(!getParent().isImmuneToFire())
			{
                getParent().isImmuneToFire = true;
			}
			getParent().extinguish();
		}
	}

	@Override
	public void kill() 
	{
        getParent().isImmuneToFire = false;
	}

	@Override
	public Ability clone() 
	{
		return new AbilityFireImmunity();
	}

	@Override
	public void postRender() {}

	@Override
	public void save(NBTTagCompound tag) {}

	@Override
	public void load(NBTTagCompound tag) {}
	
	@SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getIcon() 
	{
		return iconResource;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean entityHasAbility(EntityLivingBase living)
	{
		if(living instanceof EntitySkeleton)
		{
			EntitySkeleton skele = (EntitySkeleton)living;
			if(skele.getSkeletonType() != 1)
			{
				return false;
			}
		}
		return true;
	}
	
	public static final ResourceLocation iconResource = new ResourceLocation("morph", "textures/icon/fireImmunity.png");


}
