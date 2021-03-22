package de.cyzetlc.handler;

import de.cyzetlc.Main;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class TeamChat implements Listener
{
	@EventHandler
	public void handleChat(ChatEvent e)
	{
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		
		String[] str = e.getMessage().split(" ");
		
		if(checkIfBlocked(e.getMessage().toLowerCase()))
		{
			e.setCancelled(true);
			p.sendMessage(Main.PREFIX + 
					"§cDieser Command ist geblockt!");
		}
		
		if(str[0].equalsIgnoreCase("/tc") || str[0].equalsIgnoreCase("/teamchat"))
		{
			e.setCancelled(true);
			
			if(!p.hasPermission("team.chat"))
			{
				p.sendMessage(Main.PREFIX + "§cDazu hast du keine Rechte");
				return;
			}
			
			if(e.getMessage().equalsIgnoreCase("/tc") || e.getMessage().equalsIgnoreCase("/teamchat"))
			{
				p.sendMessage(Main.PREFIX + "§cDu musst eine Nachricht angeben!");
				return;
			}
			
			String msg = Main.PREFIX + "§a" + p.getDisplayName() + " §7: ";
			
			
			for(int i = 1; i < str.length; i++)
			{
				msg = msg + "§7" + str[i] + " ";
			}
			
			for(ProxiedPlayer all : Main.getPlugin().getProxy().getPlayers())
			{
				if(all.hasPermission("team.chat"))
				{
					all.sendMessage(new TextComponent(msg));
				}
			}
		}else
			return;
	}
	
	public boolean checkIfBlocked(String str)
	{
		if(str.startsWith("/help") || str.startsWith("/bukkit:help"))
		{
			return true;
		}
		if(str.startsWith("/?") || str.startsWith("/bukkit:?"))
		{
			return true;
		}
		if(str.startsWith("/pl") || str.startsWith("/bukkit:pl") && !str.startsWith("/plot"))
		{
			return true;
		}
		if(str.startsWith("/plugins") || str.startsWith("/bukkit:plugin"))
		{
			return true;
		}
		if(str.startsWith("/ver") || str.startsWith("/bukkit:ver"))
		{
			return true;
		}
		if(str.startsWith("/version") || str.startsWith("/bukkit:version"))
		{
			return true;
		}
		if(str.startsWith("/about") || str.startsWith("/bukkit:about"))
		{
			return true;
		}
		return false;
	}
}
