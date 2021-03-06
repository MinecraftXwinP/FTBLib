package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.client.FTBLibClient;
import com.feed_the_beast.ftblib.events.player.IGuiProvider;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class MessageOpenGui extends MessageToClient
{
	private ResourceLocation guiId;
	private BlockPos pos;
	private NBTTagCompound nbt;
	private int windowId;

	public MessageOpenGui()
	{
	}

	public MessageOpenGui(ResourceLocation key, BlockPos p, @Nullable NBTTagCompound tag, int wid)
	{
		guiId = key;
		pos = p;
		nbt = tag;
		windowId = wid;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.GENERAL;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeResourceLocation(guiId);
		data.writePos(pos);
		data.writeNBT(nbt);
		data.writeByte(windowId);
	}

	@Override
	public void readData(DataIn data)
	{
		guiId = data.readResourceLocation();
		pos = data.readPos();
		nbt = data.readNBT();
		windowId = data.readUnsignedByte();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		IGuiProvider guiProvider = FTBLibClient.getGui(guiId);

		if (guiProvider != null && ClientUtils.MC.world.isBlockLoaded(pos))
		{
			GuiScreen g = guiProvider.getGui(ClientUtils.MC.player, pos, nbt);

			if (g != null)
			{
				ClientUtils.MC.displayGuiScreen(g);
				ClientUtils.MC.player.openContainer.windowId = windowId;
			}
		}
	}
}