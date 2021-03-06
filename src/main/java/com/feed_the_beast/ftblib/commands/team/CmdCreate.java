package com.feed_the_beast.ftblib.commands.team;

import com.feed_the_beast.ftblib.FTBLibGameRules;
import com.feed_the_beast.ftblib.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.lib.EnumTeamColor;
import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.TeamType;
import com.feed_the_beast.ftblib.net.MessageMyTeamGuiResponse;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.TextComponentHelper;

/**
 * @author LatvianModder
 */
public class CmdCreate extends CmdBase
{
	public CmdCreate()
	{
		super("create", Level.ALL);
	}

	private static boolean isValidTeamID(String s)
	{
		if (!s.isEmpty())
		{
			for (int i = 0; i < s.length(); i++)
			{
				if (!isValidChar(s.charAt(i)))
				{
					return false;
				}
			}

			return true;
		}

		return false;
	}

	private static boolean isValidChar(char c)
	{
		return c == '_' || c == '|' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (!FTBLibGameRules.canCreateTeam(server.getWorld(0)))
		{
			throw new CommandException("feature_disabled_server");
		}

		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		ForgePlayer p = getForgePlayer(player);

		if (p.hasTeam())
		{
			throw new CommandException("ftblib.lang.team.error.must_leave");
		}

		checkArgs(sender, args, 1);

		if (!isValidTeamID(args[0]))
		{
			throw new CommandException("ftblib.lang.team.id_invalid");
		}

		if (p.team.universe.getTeam(args[0]).isValid())
		{
			throw new CommandException("ftblib.lang.team.id_already_exists");
		}

		ForgeTeam team = new ForgeTeam(p.team.universe, args[0], TeamType.PLAYER);

		if (args.length > 1)
		{
			team.setColor(EnumTeamColor.NAME_MAP.get(args[1]));
		}

		p.team = team;
		team.owner = p;
		team.universe.teams.put(team.getName(), team);
		new ForgeTeamCreatedEvent(team).post();
		new ForgeTeamPlayerJoinedEvent(p).post();
		sender.sendMessage(TextComponentHelper.createComponentTranslation(sender, "ftblib.lang.team.created", team.getName()));
		new MessageMyTeamGuiResponse(p).sendTo(player);
		team.markDirty();
		p.markDirty();
	}
}