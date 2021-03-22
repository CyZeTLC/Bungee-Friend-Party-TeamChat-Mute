package de.cyzetlc.cmd;

import de.cyzetlc.Main;
import de.cyzetlc.utils.manager.party.PartyManager;
import de.cyzetlc.utils.party.Party;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Cmd_PChat extends Command
{
	public Cmd_PChat()
	{
		super("p");
	}
	
	@Override
	public void execute(CommandSender s, String[] args) 
	{
		ProxiedPlayer p = (ProxiedPlayer) s;
		
		if (!PartyManager.isPlayerInParty(p))
		{
			p.sendMessage(Main.PARTY_PREFIX + "§7Du musst in einer Party sein!");
			return;
		}
		
		Party party = PartyManager.getPartyOfPlayer(p);
		String msg = "";
		
		for (int i = 0; i < args.length; i++)
		{
			msg += args[i] + " ";
		}
		
		party.sendMessageFromPlayer(p, msg);
		return;
	}
}
