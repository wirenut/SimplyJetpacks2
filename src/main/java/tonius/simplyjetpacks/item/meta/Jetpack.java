package tonius.simplyjetpacks.item.meta;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import tonius.simplyjetpacks.client.model.PackModelType;
import tonius.simplyjetpacks.config.Config;
import tonius.simplyjetpacks.handler.SyncHandler;
import tonius.simplyjetpacks.item.ItemPack;
import tonius.simplyjetpacks.setup.ParticleType;
import tonius.simplyjetpacks.util.StackUtils;
import tonius.simplyjetpacks.util.StringUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Jetpack extends PackBase {
    
    protected static final String TAG_HOVERMODE_ON = "JetpackHoverModeOn";
    protected static final String TAG_EHOVER_ON = "JetpackEHoverOn";
    protected static final String TAG_PARTICLE = "JetpackParticleType";
    
    public double speedVertical = 0.0D;
    public double accelVertical = 0.0D;
    public double speedVerticalHover = 0.0D;
    public double speedVerticalHoverSlow = 0.0D;
    public double speedSideways = 0.0D;
    public double sprintSpeedModifier = 0.0D;
    public double sprintFuelModifier = 0.0D;
    public boolean emergencyHoverMode = false;
    
    public ParticleType defaultParticleType = ParticleType.DEFAULT;
    
    public Jetpack(int tier, EnumRarity rarity, String defaultConfigKey) {
        super("jetpack", tier, rarity, defaultConfigKey);
        this.setArmorModel(PackModelType.JETPACK);
    }
    
    public Jetpack setDefaultParticleType(ParticleType defaultParticleType) {
        this.defaultParticleType = defaultParticleType;
        return this;
    }
    
    @Override
    public void tickArmor(World world, EntityPlayer player, ItemStack stack, ItemPack item) {
        this.flyUser(player, stack, item, false);
    }
    
    public void flyUser(EntityLivingBase user, ItemStack stack, ItemPack item, boolean force) {
        if (this.isOn(stack)) {
            boolean hoverMode = this.isHoverModeOn(stack);
            double hoverSpeed = Config.invertHoverSneakingBehavior == SyncHandler.isDescendKeyDown(user) ? this.speedVerticalHoverSlow : this.speedVerticalHover;
            boolean flyKeyDown = force || SyncHandler.isFlyKeyDown(user);
            boolean descendKeyDown = SyncHandler.isDescendKeyDown(user);
            double currentAccel = user.motionY < 0.3D ? this.accelVertical * 2.5 : this.accelVertical;
            
            if (flyKeyDown || hoverMode && !user.onGround) {
                if (this.usesFuel) {
                    item.useFuel(stack, (int) (user.isSprinting() ? Math.round(this.fuelUsage * this.sprintFuelModifier) : this.fuelUsage), false);
                }
                
                if (item.getFuelStored(stack) > 0) {
                    if (flyKeyDown) {
                        if (!hoverMode) {
                            user.motionY = Math.min(user.motionY + currentAccel, this.speedVertical);
                        } else {
                            if (descendKeyDown) {
                                user.motionY = Math.min(user.motionY + currentAccel, -this.speedVerticalHoverSlow);
                            } else {
                                user.motionY = Math.min(user.motionY + currentAccel, this.speedVerticalHover);
                            }
                        }
                    } else {
                        user.motionY = Math.min(user.motionY + currentAccel, -hoverSpeed);
                    }
                    
                    float speedSideways = (float) (user.isSneaking() ? this.speedSideways * 0.5F : this.speedSideways);
                    float speedForward = (float) (user.isSprinting() ? speedSideways * this.sprintSpeedModifier : speedSideways);
                    if (SyncHandler.isForwardKeyDown(user)) {
                        user.moveFlying(0, speedForward, speedForward);
                    }
                    if (SyncHandler.isBackwardKeyDown(user)) {
                        user.moveFlying(0, -speedSideways, speedSideways * 0.8F);
                    }
                    if (SyncHandler.isLeftKeyDown(user)) {
                        user.moveFlying(speedSideways, 0, speedSideways);
                    }
                    if (SyncHandler.isRightKeyDown(user)) {
                        user.moveFlying(-speedSideways, 0, speedSideways);
                    }
                    
                    if (!user.worldObj.isRemote) {
                        user.fallDistance = 0.0F;
                        if (user instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) user).playerNetServerHandler.floatingTickCount = 0;
                        }
                    }
                }
            }
        }
        
        if (!user.worldObj.isRemote && this.emergencyHoverMode && this.isEHoverOn(stack)) {
            if (item.getEnergyStored(stack) > 0 && (!this.isHoverModeOn(stack) || !this.isOn(stack))) {
                if (user.posY < -5) {
                    this.doEHover(stack, user);
                } else if (user instanceof EntityPlayer) {
                    if (!((EntityPlayer) user).capabilities.isCreativeMode && user.fallDistance - 1.2F >= user.getHealth()) {
                        for (int i = 0; i <= 16; i++) {
                            if (!user.worldObj.isAirBlock(Math.round((float) user.posX - 0.5F), Math.round((float) user.posY) - i, Math.round((float) user.posZ - 0.5F))) {
                                this.doEHover(stack, user);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void doEHover(ItemStack armor, EntityLivingBase user) {
        StackUtils.getNBT(armor).setBoolean(TAG_ON, true);
        StackUtils.getNBT(armor).setBoolean(TAG_HOVERMODE_ON, true);
        if (user instanceof EntityPlayer) {
            ((EntityPlayer) user).addChatMessage(new ChatComponentText(StringUtils.LIGHT_RED + StringUtils.translate("chat.jetpack.emergencyHoverMode.msg")));
        }
    }
    
    public void setMobMode(ItemStack itemStack) {
        itemStack.stackTagCompound.setBoolean("JetpackOn", true);
        itemStack.stackTagCompound.setBoolean("JetpackHoverModeOn", false);
    }
    
    public boolean isHoverModeOn(ItemStack stack) {
        return StackUtils.getNBTBoolean(stack, TAG_HOVERMODE_ON, false);
    }
    
    public boolean isEHoverOn(ItemStack stack) {
        return StackUtils.getNBTBoolean(stack, TAG_EHOVER_ON, false);
    }
    
    @Override
    public void switchMode(ItemStack stack, EntityPlayer player, boolean sneakChangesToggleBehavior, boolean showInChat) {
        if (!player.isSneaking() || !this.emergencyHoverMode || !sneakChangesToggleBehavior) {
            this.switchHoverMode(stack, player, showInChat);
        } else {
            this.switchEHover(stack, player);
        }
    }
    
    protected void switchHoverMode(ItemStack stack, EntityPlayer player, boolean showInChat) {
        this.toggleState(this.isHoverModeOn(stack), stack, "hoverMode", TAG_HOVERMODE_ON, player, showInChat);
    }
    
    public void switchEHover(ItemStack stack, EntityPlayer player) {
        this.toggleState(this.isEHoverOn(stack), stack, "emergencyHoverMode", TAG_EHOVER_ON, player, true);
    }
    
    public void setParticleType(ItemStack stack, ParticleType particle) {
        StackUtils.getNBT(stack).setInteger(TAG_PARTICLE, particle.ordinal());
    }
    
    protected ParticleType getParticleType(ItemStack stack) {
        if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(TAG_PARTICLE)) {
            int particle = StackUtils.getNBT(stack).getInteger(TAG_PARTICLE);
            ParticleType particleType = ParticleType.values()[particle];
            if (particleType != null) {
                return particleType;
            }
        }
        StackUtils.getNBT(stack).setInteger(TAG_PARTICLE, this.defaultParticleType.ordinal());
        return this.defaultParticleType;
    }
    
    public ParticleType getDisplayParticleType(ItemStack stack, ItemPack item, EntityLivingBase user) {
        boolean flyKeyDown = SyncHandler.isFlyKeyDown(user);
        if (this.isOn(stack) && item.getFuelStored(stack) > 0 && (flyKeyDown || this.isHoverModeOn(stack) && !user.onGround && user.motionY < 0)) {
            return this.getParticleType(stack);
        }
        return null;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addShiftInformation(ItemStack stack, ItemPack item, EntityPlayer player, List list) {
        list.add(StringUtils.getStateText(this.isOn(stack)));
        list.add(StringUtils.getHoverModeText(this.isHoverModeOn(stack)));
        if (this.fuelUsage > 0) {
            list.add(StringUtils.getFuelUsageText(this.fuelType, this.fuelUsage));
        }
        list.add(StringUtils.getArmoredText(this.isArmored));
        list.add(StringUtils.getParticlesText(this.getParticleType(stack)));
        StringUtils.addDescriptionLines(list, "jetpack", StringUtils.BRIGHT_GREEN);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public String getHUDStatesInfo(ItemStack stack, ItemPack item) {
        Boolean engine = this.isOn(stack);
        Boolean hover = this.isHoverModeOn(stack);
        return StringUtils.getHUDStateText(engine, hover, null);
    }
    
    // start configuration
    
    @Override
    protected void loadConfig(Configuration config) {
        super.loadConfig(config);
        
        if (this.defaults.speedVertical != null) {
            this.speedVertical = config.get(this.defaults.section.name, "Vertical Speed", this.defaults.speedVertical, "The maximum vertical speed of this jetpack when flying.").setMinValue(0.0D).getDouble(this.defaults.speedVertical);
        }
        if (this.defaults.accelVertical != null) {
            this.accelVertical = config.get(this.defaults.section.name, "Vertical Acceleration", this.defaults.accelVertical, "The vertical acceleration of this jetpack when flying; every tick, this amount of vertical speed will be added until maximum speed is reached.").setMinValue(0.0D).getDouble(this.defaults.accelVertical);
        }
        if (this.defaults.speedVerticalHover != null) {
            this.speedVerticalHover = config.get(this.defaults.section.name, "Vertical Speed (Hover Mode)", this.defaults.speedVerticalHover, "The maximum vertical speed of this jetpack when flying in hover mode.").setMinValue(0.0D).getDouble(this.defaults.speedVerticalHover);
        }
        if (this.defaults.speedVerticalHoverSlow != null) {
            this.speedVerticalHoverSlow = config.get(this.defaults.section.name, "Vertical Speed (Hover Mode / Slow Descent)", this.defaults.speedVerticalHoverSlow, "The maximum vertical speed of this jetpack when slowly descending in hover mode.").setMinValue(0.0D).getDouble(this.defaults.speedVerticalHoverSlow);
        }
        if (this.defaults.speedSideways != null) {
            this.speedSideways = config.get(this.defaults.section.name, "Sideways Speed", this.defaults.speedSideways, "The speed of the jetpack when flying sideways. This is mostly noticeable in hover mode.").setMinValue(0.0D).getDouble(this.defaults.speedSideways);
        }
        if (this.defaults.sprintSpeedModifier != null) {
            this.sprintSpeedModifier = config.get(this.defaults.section.name, "Sprint Speed Multiplier", this.defaults.sprintSpeedModifier, "How much faster the jetpack will fly forward when sprinting. Setting this to 1.0 will make sprinting have no effect apart from the added speed from vanilla.").setMinValue(0.0D).getDouble(this.defaults.sprintSpeedModifier);
        }
        if (this.defaults.sprintFuelModifier != null) {
            this.sprintFuelModifier = config.get(this.defaults.section.name, "Sprint Fuel Usage Multiplier", this.defaults.sprintFuelModifier, "How much more energy the jetpack will use when sprinting. Setting this to 1.0 will make sprinting have no effect on energy usage.").setMinValue(0.0D).getDouble(this.defaults.sprintFuelModifier);
        }
    }
    
    @Override
    protected void writeConfigToNBT(NBTTagCompound tag) {
        super.writeConfigToNBT(tag);
        
        if (this.defaults.speedVertical != null) {
            tag.setDouble("SpeedVertical", this.speedVertical);
        }
        if (this.defaults.accelVertical != null) {
            tag.setDouble("AccelVertical", this.accelVertical);
        }
        if (this.defaults.speedVerticalHover != null) {
            tag.setDouble("SpeedVerticalHover", this.speedVerticalHover);
        }
        if (this.defaults.speedVerticalHoverSlow != null) {
            tag.setDouble("SpeedVerticalHoverSlow", this.speedVerticalHoverSlow);
        }
        if (this.defaults.speedSideways != null) {
            tag.setDouble("SpeedSideways", this.speedSideways);
        }
        if (this.defaults.sprintSpeedModifier != null) {
            tag.setDouble("SprintSpeedModifier", this.sprintSpeedModifier);
        }
        if (this.defaults.sprintFuelModifier != null) {
            tag.setDouble("SprintFuelModifier", this.sprintFuelModifier);
        }
    }
    
    @Override
    protected void readConfigFromNBT(NBTTagCompound tag) {
        super.readConfigFromNBT(tag);
        
        if (this.defaults.speedVertical != null) {
            this.speedVertical = tag.getDouble("SpeedVertical");
        }
        if (this.defaults.accelVertical != null) {
            this.accelVertical = tag.getDouble("AccelVertical");
        }
        if (this.defaults.speedVerticalHover != null) {
            this.speedVerticalHover = tag.getDouble("SpeedVerticalHover");
        }
        if (this.defaults.speedVerticalHoverSlow != null) {
            this.speedVerticalHoverSlow = tag.getDouble("SpeedVerticalHoverSlow");
        }
        if (this.defaults.speedSideways != null) {
            this.speedSideways = tag.getDouble("SpeedSideways");
        }
        if (this.defaults.sprintSpeedModifier != null) {
            this.sprintSpeedModifier = tag.getDouble("SprintSpeedModifier");
        }
        if (this.defaults.sprintFuelModifier != null) {
            this.sprintFuelModifier = tag.getDouble("SprintFuelModifier");
        }
    }
    
}
