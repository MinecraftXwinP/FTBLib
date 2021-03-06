package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import com.feed_the_beast.ftblib.lib.data.Action;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class RegisterTeamGuiActionsEvent extends FTBLibEvent
{
	private Consumer<Action> callback;

	public RegisterTeamGuiActionsEvent(Consumer<Action> c)
	{
		callback = c;
	}

	public void register(Action action)
	{
		callback.accept(action);
	}
}