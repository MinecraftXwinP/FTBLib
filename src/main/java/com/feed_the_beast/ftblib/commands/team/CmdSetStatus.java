package com.feed_the_beast.ftblib.commands.team;

import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdSetStatus extends CmdBase
{
	public CmdSetStatus()
	{
		super("set_status", Level.ALL);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 2)
		{
			return getListOfStringsMatchingLastWord(args, EnumTeamStatus.VALID_VALUES);
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public boolean isUsernameIndex(String[] args, int i)
	{
		return i == 0;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		ForgePlayer p = getForgePlayer(getCommandSenderAsPlayer(sender));

		if (!p.hasTeam())
		{
			throw new CommandException("ftblib.lang.team.error.no_team");
		}
		else if (!p.team.isModerator(p))
		{
			throw new CommandException("commands.generic.permission");
		}

		checkArgs(sender, args, 2);
		ForgePlayer p1 = getForgePlayer(sender, args[0]);

		if (p.team.isOwner(p1))
		{
			throw new CommandException("ftblib.lang.team.permission.owner");
		}
		else if (!p.team.isModerator(p))
		{
			throw new CommandException("commands.generic.permission");
		}

		EnumTeamStatus status = EnumTeamStatus.NAME_MAP.get(args[1].toLowerCase());

		if (status.canBeSet())
		{
			p.team.setStatus(p1, status);
		}

		//TODO: Display notification
	}
}
