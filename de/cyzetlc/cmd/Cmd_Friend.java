package de.cyzetlc.cmd;

import de.cyzetlc.Main;
import de.cyzetlc.utils.manager.friends.FriendManager;
import de.cyzetlc.utils.mojang.UUIDUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Cmd_Friend extends Command
{
	public Cmd_Friend()
	{
		super ("friend");
	}
	
	@Override
	public void execute(CommandSender s, String[] args) 
	{
		ProxiedPlayer p = (ProxiedPlayer) s;
		
		if (args.length == 0)
		{
			this.sendHelpInfo(p);
			return;
		}
		
		if (args.length == 1)
		{
			if (args[0].equalsIgnoreCase("invites"))
			{
				if (FriendManager.getInvites(p.getUniqueId().toString()).size() == 0)
				{
					p.sendMessage(Main.FRIEND_PREFIX + "§7Du hast keine Freundes Anfragen!");
					return;
				}
				
				p.sendMessage(Main.FRIEND_PREFIX + "§7Alle Freundes Anfragen:");
				
				String result = Main.FRIEND_PREFIX;
				
				for (String all : FriendManager.getInvites(p.getUniqueId().toString()))
				{
					result += "§a" + all + "§7, ";
				}
					
				p.sendMessage(result);
				return;
			}
			
			if (args[0].equalsIgnoreCase("list"))
			{
				if (FriendManager.getFriends(p.getUniqueId().toString()).size() == 0)
				{
					p.sendMessage(Main.FRIEND_PREFIX + "§7Du hast keine Freunde in deiner Freundesliste!");
					return;
				}
				
				p.sendMessage(Main.FRIEND_PREFIX + "§7Alle §aFreunde §7aus deiner Freundesliste:");
				
				String result = Main.FRIEND_PREFIX;
				
				for (String all : FriendManager.getFriends(p.getUniqueId().toString()))
				{
					result += "§a" + all + "§7, ";
				}
					
				p.sendMessage(result);
				return;
			}
			else
			{
				this.sendHelpInfo(p);
				return;
			}
		}
		
		if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("add"))
			{
				String uuidFriend = "";
				
				if (FriendManager.getFriends(p.getUniqueId().toString()).size() > 25)
				{
					p.sendMessage(Main.FRIEND_PREFIX + "§7Du hast die maximale Anzahl an Freunden erreicht!");
					return;
				}

				if (FriendManager.isPlayerHaveFriend(p, args[1]))
				{
					p.sendMessage(Main.FRIEND_PREFIX + "§7Du bist bereits mit dieser Person befreundet!");
					return;
				}
				
				if (ProxyServer.getInstance().getPlayer(args[1]) == null) 
				{	
					uuidFriend = UUIDUtil.getUUID(args[1]);
				}
				else
				{
					uuidFriend = ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString();
				}

				if (uuidFriend == null)
				{
					p.sendMessage(Main.FRIEND_PREFIX + "§7Dieser Spieler wurde nicht gefunden!");
					return;
				}
				
				if (FriendManager.isPlayerHaveFriend(p, args[1])) 
				{
					p.sendMessage(Main.FRIEND_PREFIX + "§7Du bist bereits mit diesem Spieler befreundet!");
					return;
				}

				FriendManager.inviteFriend(p, uuidFriend, args[1]);
				p.sendMessage(Main.FRIEND_PREFIX + "§7Du hast §a" + args[1] + "§7 eine Freundes Anfrage gesendet.");
				return;
			}
			
			if (args[0].equalsIgnoreCase("jump"))
			{
				if (!FriendManager.isPlayerHaveFriend(p, args[1]))
				{
					p.sendMessage(Main.FRIEND_PREFIX + "§7Du bist mit diesem Spieler nicht befreundet!");
					return;
				}
				
				if (ProxyServer.getInstance().getPlayer(args[1]) == null) 
				{	
					p.sendMessage(Main.FRIEND_PREFIX + "§7Dieser Spieler ist nicht online!");
					return;
				}
				
				p.sendMessage(Main.FRIEND_PREFIX + "§7Du wirst mit dem Server von §a" + args[1] + "§7 verbunden..");
				p.connect(ProxyServer.getInstance().getPlayer(args[1]).getServer().getInfo());
				return;
			}
			
			if (args[0].equalsIgnoreCase("remove"))
			{
				if (!FriendManager.isPlayerHaveFriend(p, args[1]))
				{
					p.sendMessage(Main.FRIEND_PREFIX + "§7Du bist mit diesem Spieler nicht befreundet!");
					return;
				}
				
				String uuidFriend = "";
				
				if (ProxyServer.getInstance().getPlayer(args[1]) == null) 
				{	
					uuidFriend = UUIDUtil.getUUID(args[1]);
				}
				else
				{
					uuidFriend = ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString();
				}
				
				FriendManager.removeFriend(p.getUniqueId().toString(), uuidFriend, args[1]);
				
				p.sendMessage(Main.FRIEND_PREFIX + "§7Du hast den Spieler §a" + args[1] + "§7 aus deiner Freundesliste gelöscht.");
				return;
			}
			
			if (args[0].equalsIgnoreCase("deny"))
			{
				if (!FriendManager.isPlayerHaveInvite(p, args[1]))
				{
					p.sendMessage(Main.FRIEND_PREFIX + "§7Du hast keine Einladung von diesem Spieler erhalten!");
					return;
				}
				
				String uuidFriend = "";
				
				if (ProxyServer.getInstance().getPlayer(args[1]) == null) 
				{	
					uuidFriend = UUIDUtil.getUUID(args[1]);
				}
				else
				{
					uuidFriend = ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString();
				}
				
				FriendManager.removeInvite(p.getUniqueId().toString(), uuidFriend, args[1]);
				
				p.sendMessage(Main.FRIEND_PREFIX + "§7Du hast die Anfrage von §a" + args[1] + "§7 abgelehnt.");
				return;
			}
			
			if (args[0].equalsIgnoreCase("accept"))
			{
				if (!FriendManager.isPlayerHaveInvite(p, args[1]))
				{
					p.sendMessage(Main.FRIEND_PREFIX + "§7Du hast keine Einladung von diesem Spieler erhalten!");
					return;
				}
				
				String uuidFriend = "";
				
				if (ProxyServer.getInstance().getPlayer(args[1]) == null) 
				{	
					uuidFriend = UUIDUtil.getUUID(args[1]);
				}
				else
				{
					uuidFriend = ProxyServer.getInstance().getPlayer(args[1]).getUniqueId().toString();
				}
				
				FriendManager.removeInvite(p.getUniqueId().toString(), uuidFriend, args[1]);
				FriendManager.addFriend(p.getUniqueId().toString(), uuidFriend, args[1]);
				FriendManager.addFriend(uuidFriend, p.getUniqueId().toString(), p.getName());
				
				if (ProxyServer.getInstance().getPlayer(args[1]) != null)
				{
					ProxyServer.getInstance().getPlayer(args[1]).sendMessage(Main.FRIEND_PREFIX + "§7Du bist nun mit §a" + p.getName() + "§7 befreundet.");
				}
				
				p.sendMessage(Main.FRIEND_PREFIX + "§7Du bist nun mit §a" + args[1] + "§7 befreundet.");
				return;
			}
			else
			{
				this.sendHelpInfo(p);
				return;
			}
		}
		else
		{
			this.sendHelpInfo(p);
		}
	}
	
	private void sendHelpInfo(ProxiedPlayer p)
	{
		p.sendMessage(Main.FRIEND_PREFIX);
		p.sendMessage("§8>> §7/friend add <SPIELER> §8- §7Sende einen SPieler eine Freundes Anfrage.");
		p.sendMessage("§8>> §7/friend accept <SPIELER> §8- §7Nehme eine Freundes Anfrage an.");
		p.sendMessage("§8>> §7/friend deny <SPIELER> §8- §7Lehne eine Freundes Anfrage ab.");
		p.sendMessage("§8>> §7/friend remove <SPIELER> §8- §7Lösche einen Freund aus deiner Freundesliste.");
		p.sendMessage("§8>> §7/friend jump <SPIELER> §8- §7Betrete den Server eines Freundes.");
		p.sendMessage("§8>> §7/friend list §8- §7Zeige alle deine Freunde.");
	}
}
