package de.cyzetlc.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.cyzetlc.Main;
import de.cyzetlc.utils.Date;
import de.cyzetlc.utils.Mute;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatHandler implements Listener
{
	public static final HashMap<ProxiedPlayer, String> lastMsg = new HashMap<>();
	public static final List<String> blocked = new ArrayList<>();
	
	@EventHandler
	public void handleCheckMute(ChatEvent e)
	{
		final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		Mute m = Mute.getMuteByUUID(p.getUniqueId().toString());
		
		if (m != null && !e.isCancelled())
		{
			if (e.getMessage().startsWith("/") && !e.getMessage().startsWith("/msg")) return;
			
			if (m.getTill() > System.currentTimeMillis())
			{
				e.setCancelled(true);
				p.sendMessage(Main.PREFIX);
				p.sendMessage("");
				p.sendMessage("§7Du bist gemutet.");
				p.sendMessage("§7Grund: §e" + m.getReason());
				p.sendMessage("§7Bis: §e" + Date.getDateFormat(m.getTill()));
				p.sendMessage("");
				p.sendMessage(Main.PREFIX);
			}
			else
			{
				Main.mutes.remove(m);
			}
		}
	}
	
	@EventHandler
	public void handleCheckUppercase(ChatEvent e)
	{
		final String m = e.getMessage().trim();
		final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		float uppercase = 0;
		
		for (int i = 0; i < m.length(); i++)
		{
			if (Character.isUpperCase(m.charAt(i)) && Character.isLetter(m.charAt(i)))
			{
				uppercase++;
			}
		}
		
		if ((uppercase / ((float) m.length())) > 0.6F)
		{
			if (!e.isCancelled() && !e.getMessage().startsWith("/"))
			{
				e.setCancelled(true);
				p.sendMessage(Main.PREFIX + "§cBitte deaktiviere deine Feststelltaste.");
			}
		}
	}
	
	@EventHandler
	public void handleCheckSimular(ChatEvent e)
	{
		final String m = e.getMessage();
		final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		
		if (!(lastMsg.containsKey(p)))
		{
			if (!m.startsWith("/"))
			{
				lastMsg.put(p, m);
			}
			return;
		}
		
		if (lastMsg.get(p).contains(m))
		{
			if (!e.isCancelled() && !m.startsWith("/"))
			{
				e.setCancelled(true);
				p.sendMessage(Main.PREFIX + "§cDu wiederholst dich."); 
			}
		}
		
		if (!m.startsWith("/"))
		{
			lastMsg.put(p, m);
		}
	}
	
	@EventHandler
	public void handleCheckSpam(ChatEvent e)
	{
		final String m = e.getMessage();
		final String[] args = m.split("");
		String last = "";
		final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		float lastCount = 0;
		boolean isSpam = false;
		
		for (int i = 0; i < args.length; i++)
		{
			if (lastCount > 3)
			{
				isSpam = true;
				break;
			}
			
			if (last.equalsIgnoreCase(args[i]))
			{
				lastCount++;
			}
			else
			{
				lastCount = 0;
			}
			
			last = args[i];
		}
		
		if (isSpam)
		{
			if (!e.isCancelled() && !e.getMessage().startsWith("/"))
			{
				e.setCancelled(true);
				p.sendMessage(Main.PREFIX + "§cBitte nutze keine Spamzeichen.");
			}
		}
	}
	
	@EventHandler
	public void handleCheckBlock(ChatEvent e)
	{
		final String m = e.getMessage();
		final String[] args = m.split(" ");
		final ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		boolean isUsingBlocked = false;
		
		for (String s : blocked)
		{
			for (String msg : args)
			{
				if (msg.toLowerCase().equals(s))
				{
					isUsingBlocked = true;
					break;
				}
			}
		}
		
		if (isUsingBlocked)
		{
			if (!e.isCancelled())
			{
				e.setCancelled(true);
				p.sendMessage(Main.PREFIX + "§cBitte achte auf deine Wortwahl.");
				
				long till = System.currentTimeMillis() + (((1 * 60) * 60) * 1000);
								
				if (Mute.getMuteByUUID(p.getUniqueId().toString()) != null)
				{
					return;
				}
				
				Mute mute = Mute.createNewMute(p.getUniqueId().toString(), "System", "Automatischer Mute", till, true);
			}
		}
	}
}
