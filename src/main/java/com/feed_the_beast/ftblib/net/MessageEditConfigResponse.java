package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToServer;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * @author LatvianModder
 */
public class MessageEditConfigResponse extends MessageToServer
{
	private JsonObject groupData;

	public MessageEditConfigResponse()
	{
	}

	public MessageEditConfigResponse(JsonObject json)
	{
		groupData = json;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.EDIT_CONFIG;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeJson(groupData);
	}

	@Override
	public void readData(DataIn data)
	{
		groupData = data.readJson().getAsJsonObject();
	}

	@Override
	public void onMessage(EntityPlayerMP player)
	{
		FTBLibCommon.EditingConfig c = FTBLibCommon.TEMP_SERVER_CONFIG.get(player.getGameProfile().getId());
		//TODO: Logger

		if (c != null)
		{
			c.callback.saveConfig(c.group, player, groupData);
			FTBLibCommon.TEMP_SERVER_CONFIG.remove(player.getUniqueID());
		}
	}
}