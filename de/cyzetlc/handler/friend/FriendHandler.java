package de.cyzetlc.handler.friend;

import de.cyzetlc.Main;
import de.cyzetlc.utils.manager.friends.FriendManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class FriendHandler implements Listener
{
	@EventHandler
	public void handleJoin(PostLoginEvent e)
	{
		ProxiedPlayer p = e.getPlayer();
		
		for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			if (FriendManager.isPlayerHaveFriend(all, p.getName()))
			{
				all.sendMessage(Main.FRIEND_PREFIX + "§7Dein Freund §a" + p.getName() + "§7 ist nun §aonline§7.");
			}
		}
		
		int online = 0;
		for (int i = 0; i < FriendManager.getFriends(p.getUniqueId().toString()).size(); i++)
		{
			if (ProxyServer.getInstance().getPlayer(FriendManager.getFriends(p.getUniqueId().toString()).get(i)) != null)
			{
				online++;
			}
		}
		
		p.sendMessage(Main.FRIEND_PREFIX + "§7Aktuell sind §a" + online + "§7 deiner Freunde online.");
		
		int newInvites = 0;
		for (int i = 0; i < FriendManager.getInvites(p.getUniqueId().toString()).size(); i++)
		{
			newInvites++;
		}
		
		if (newInvites > 0)
		{
			p.sendMessage(Main.FRIEND_PREFIX + "§7Du hast §a" + newInvites + "§7 neue Freundes Anfragen.");
			p.sendMessage(Main.FRIEND_PREFIX + "§7Nutze §a/friend invites");
		}
	}
	
	@EventHandler
	public void handleQuit(PlayerDisconnectEvent e)
	{
		ProxiedPlayer p = e.getPlayer();
		
		for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			if (FriendManager.isPlayerHaveFriend(all, p.getName()))
			{
				all.sendMessage(Main.FRIEND_PREFIX + "§7Dein Freund §a" + p.getName() + "§7 ist nun §coffline§7.");
			}
		}
	}
}
