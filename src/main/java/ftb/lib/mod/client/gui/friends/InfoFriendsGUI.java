package ftb.lib.mod.client.gui.friends;

import ftb.lib.api.*;
import ftb.lib.api.info.InfoPage;
import latmod.lib.LMColor;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

/**
 * Created by LatvianModder on 23.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoFriendsGUI extends InfoPage
{
	public InfoFriendsGUI()
	{
		super("friends_gui");
		setTitle(new TextComponentString("FriendsGUI"));
		
		List<ForgePlayer> tempPlayerList = new ArrayList<>();
		tempPlayerList.addAll(ForgeWorldSP.inst.playerMap.values());
		
		tempPlayerList.remove(ForgeWorldSP.inst.clientPlayer);
		
		//if(FTBUClient.sort_friends_az.get()) Collections.sort(tempPlayerList, LMPNameComparator.instance);
		//else Collections.sort(tempPlayerList, new LMPStatusComparator(LMWorldClient.inst.clientPlayer));
		
		Collections.sort(tempPlayerList, new ForgePlayerComparators.ByStatus(ForgeWorldSP.inst.clientPlayer));
		
		addSub(new InfoFriendsGUISelfPage());
		
		for(ForgePlayer p : tempPlayerList)
		{
			addSub(new InfoFriendsGUIPage(p.toPlayerSP()));
		}
	}
	
	public LMColor getBackgroundColor()
	{ return new LMColor.RGB(30, 30, 30); }
	
	public LMColor getTextColor()
	{ return new LMColor.RGB(200, 200, 200); }
	
	public Boolean useUnicodeFont()
	{ return Boolean.FALSE; }
}