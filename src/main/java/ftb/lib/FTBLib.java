package ftb.lib;

import java.io.File;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.logging.log4j.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.mod.*;
import latmod.lib.*;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;

public class FTBLib
{
	public static final Logger logger = LogManager.getLogger("FTBLib");
	public static final String FORMATTING = "\u00a7";
	public static final Pattern textFormattingPattern = Pattern.compile("(?i)" + FORMATTING + "[0-9A-FK-OR]");
	
	public static File folderConfig;
	public static File folderMinecraft;
	public static File folderModpack;
	public static File folderLocal;
	
	public static void init(File c)
	{
		folderConfig = c;
		folderMinecraft = c.getParentFile();
		folderModpack = new File(folderMinecraft, "modpack/");
		folderLocal = new File(folderMinecraft, "local/");
		
		if(!folderModpack.exists()) folderModpack.mkdirs();
		if(!folderLocal.exists()) folderLocal.mkdirs();
	}
	
	public static final Configuration loadConfig(FMLPreInitializationEvent e, String s)
	{ return new Configuration(new File(e.getModConfigurationDirectory(), s)); }
	
	public static IChatComponent getChatComponent(Object o)
	{ return (o != null && o instanceof IChatComponent) ? (IChatComponent)o : new ChatComponentText("" + o); }
	
	/** Prints message to chat (doesn't translate it) */
	public static void printChat(ICommandSender ep, Object o)
	{
		if(ep == null && FTBLibFinals.DEV) ep = FTBLibMod.proxy.getClientPlayer();
		if(ep != null) ep.addChatMessage(getChatComponent(o));
		else logger.info(o);
	}
	
	public static MinecraftServer getServer()
	{ return FMLCommonHandler.instance().getMinecraftServerInstance(); }
	
	public static boolean isServer()
	{ return getEffectiveSide().isServer(); }
	
	public static Side getEffectiveSide()
	{ return FMLCommonHandler.instance().getEffectiveSide(); }
	
	public static String getPath(ResourceLocation res)
	{ return "/assets/" + res.getResourceDomain() + "/" + res.getResourcePath(); }
	
	public static boolean resourceExists(ResourceLocation res)
	{ return FTBLib.class.getResource(getPath(res)) != null; }
	
	public static boolean hasOnlinePlayers()
	{ return !getServer().getConfigurationManager().playerEntityList.isEmpty(); }
	
	@SuppressWarnings("unchecked")
	public static FastList<EntityPlayerMP> getAllOnlinePlayers(EntityPlayerMP except)
	{
		FastList<EntityPlayerMP> l = new FastList<EntityPlayerMP>();
		if(hasOnlinePlayers())
		{
			l.addAll(getServer().getConfigurationManager().playerEntityList);
			if(except != null) l.removeObj(except);
		}
		return l;
	}
	
	public static FastMap<UUID, EntityPlayerMP> getAllOnlinePlayersMap()
	{
		FastMap<UUID, EntityPlayerMP> m = new FastMap<UUID, EntityPlayerMP>();
		
		if(!hasOnlinePlayers()) return m;
		
		for(int i = 0; i < getServer().getConfigurationManager().playerEntityList.size(); i++)
		{
			EntityPlayerMP ep = (EntityPlayerMP)getServer().getConfigurationManager().playerEntityList.get(i);
			m.put(ep.getUniqueID(), ep);
		}
		
		return m;
	}
	
	public static EntityPlayerMP getPlayerMP(UUID id)
	{
		if(!hasOnlinePlayers()) return null;
		
		for(int i = 0; i < getServer().getConfigurationManager().playerEntityList.size(); i++)
		{
			EntityPlayerMP ep = (EntityPlayerMP)getServer().getConfigurationManager().playerEntityList.get(i);
			if(ep.getUniqueID().equals(id)) return ep;
		}
		
		return null;
	}
	
	public static boolean remap(MissingMapping m, String id, Item i)
	{
		if(m.type == GameRegistry.Type.ITEM && id.equals(m.name))
		{
			m.remap(i);
			return true;
		}
		
		return false;
	}
	
	public static boolean remap(MissingMapping m, String id, Block b)
	{
		if(id.equals(m.name))
		{
			if(m.type == GameRegistry.Type.BLOCK) m.remap(b);
			else if(m.type == GameRegistry.Type.ITEM) m.remap(Item.getItemFromBlock(b));
			return true;
		}
		
		return false;
	}
	
	public static boolean isModInstalled(String s)
	{ return Loader.isModLoaded(s); }
	
	public static String removeFormatting(String s)
	{
		if(s == null) return null; if(s.isEmpty()) return "";
		return textFormattingPattern.matcher(s).replaceAll("");
	}
	
	public static WorldServer getServerWorld()
	{
		MinecraftServer ms = getServer();
		if(ms == null || ms.worldServers.length == 0) return null;
		return ms.worldServers[0];
	}
	
	public static Exception runCommand(ICommandSender ics, String s)
	{
		try { getServer().getCommandManager().executeCommand(ics, s); }
		catch(Exception e) { return e; } return null;
	}
	
	public static Exception runCommand(ICommandSender ics, String cmd, String[] args)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(cmd);
		if(args != null && args.length > 0)
		{
			for(int i = 0; i < args.length; i++)
			{ sb.append(' '); sb.append(args[i]); }
		}
		
		return runCommand(ics, sb.toString());
	}

	public static IChatComponent setColor(EnumChatFormatting e, IChatComponent c)
	{ c.getChatStyle().setColor(e); return c; }
}