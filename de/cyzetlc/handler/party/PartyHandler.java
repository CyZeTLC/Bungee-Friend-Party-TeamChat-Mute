package de.cyzetlc.handler.party;

import de.cyzetlc.utils.manager.party.PartyManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PartyHandler implements Listener
{
	@EventHandler
	public void handleSwitch(ServerSwitchEvent e)
	{
		ProxiedPlayer p = e.getPlayer();
		
		if (PartyManager.isPartyLeader(p) && !p.getServer().getInfo().getName().startsWith("Lobby"))
		{
			for (ProxiedPlayer all : PartyManager.getPartyOfPlayer(p).getPlayers())
			{
				if (!all.equals(p))
				{
					all.connect(p.getServer().getInfo());
				}
			}
			
			PartyManager.getPartyOfPlayer(p).alert("§7Die Party betritt einen §e" + PartyManager.getServiceName(p.getServer().getInfo()) + "§7 Server.");
		}
	}
	
	@EventHandler
	public void handleQuit(PlayerDisconnectEvent e)
	{
		ProxiedPlayer p = e.getPlayer();
		
		if (PartyManager.isPlayerInParty(p))
		{
			PartyManager.getPartyOfPlayer(p).removePlayer(p);
		}
	}
}
