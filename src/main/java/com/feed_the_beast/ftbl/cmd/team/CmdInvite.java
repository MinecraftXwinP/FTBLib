package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdInvite extends CommandLM
{
    public CmdInvite()
    {
        super("invite");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        ForgePlayer p = getForgePlayer(ep);
        ForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.team_no_team.commandError();
        }
        else if(!team.getStatus(p).isOwner())
        {
            throw FTBLibLang.team_not_owner.commandError();
        }

        checkArgs(args, 1, "<player>");

        ForgePlayer p1 = getForgePlayer(args[0]);

        if(team.inviteMember(p1))
        {
            FTBLibLang.team_invited.printChat(sender, p1.getProfile().getName(), team.getName());

            if(p1.isOnline())
            {
                FTBLibLang.team_invited_you.printChat(p1.getPlayer(), team.getName(), ep.getName());
            }
        }
        else
        {
            throw FTBLibLang.team_already_invited.commandError(p1.getProfile().getName());
        }
    }
}
