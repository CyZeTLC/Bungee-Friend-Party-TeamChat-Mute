package de.cyzetlc.cmd;

import de.cyzetlc.Main;
import de.cyzetlc.utils.manager.party.PartyManager;
import de.cyzetlc.utils.party.Party;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Cmd_Party extends Command
{
	public Cmd_Party()
	{
		super ("party");
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
			if (args[0].equalsIgnoreCase("leave"))
			{
				if (!PartyManager.isPlayerInParty(p))
				{
					p.sendMessage(Main.PARTY_PREFIX + "§7Du bist nicht in einer Party!");
					return;
				}
				
				Party party = PartyManager.getPartyOfPlayer(p);
				p.sendMessage(Main.PARTY_PREFIX + "§7Du hast die Party verlassen");
				party.removePlayer(p);
				return;
			}
			
			if (args[0].equalsIgnoreCase("jump"))
			{
				if (!PartyManager.isPlayerInParty(p))
				{
					p.sendMessage(Main.PARTY_PREFIX + "§7Du bist nicht in einer Party!");
					return;
				}
				
				Party party = PartyManager.getPartyOfPlayer(p);
				p.sendMessage(Main.PARTY_PREFIX + "§7Du wirst mit dem Server des Party Leiters verbunden..");
				p.connect(party.getOwner().getServer().getInfo());
				return;
			}
			
			if (args[0].equalsIgnoreCase("list"))
			{
				if (!PartyManager.isPlayerInParty(p))
				{
					p.sendMessage(Main.PARTY_PREFIX + "§7Du bist nicht in einer Party!");
					return;
				}
				
				Party party = PartyManager.getPartyOfPlayer(p);
				String result = Main.PARTY_PREFIX;
				p.sendMessage(Main.PARTY_PREFIX + "§7Alle Spieler aus der Party von §a" + party.getOwner().getName() + "§7:");
				
				for (ProxiedPlayer all : party.getPlayers())
				{
					result += "§a" + all.getName() + "§7, ";
				}
					
				p.sendMessage(result);
				return;
			}
			
			if (args[0].equalsIgnoreCase("delete"))
			{
				if (!PartyManager.isPartyLeader(p))
				{
					p.sendMessage(Main.PARTY_PREFIX + "§7Du musst ein Leiter einer Party sein!");
					return;
				}
				
				Party party = PartyManager.getPartyOfPlayer(p);
				party.delete();
				return;
			}
			return;
		}
		
		if (args.length == 2)
		{
			if (args[0].equalsIgnoreCase("invite"))
			{
				if (!PartyManager.isPlayerInParty(p))
				{
					new Party(p);
				}
				if (PartyManager.isPartyLeader(p))
				{
					Party party = PartyManager.getPartyOfPlayer(p);
					
					if (party.getPlayers().size() > 12)
					{
						p.sendMessage(Main.PARTY_PREFIX + "§7Deine Party ist voll!");
						return;
					}
					
					if (ProxyServer.getInstance().getPlayer(args[1]) == null)
					{
						p.sendMessage(Main.PARTY_PREFIX + "§7Dieser Spieler ist nicht online!");
						return;
					}
					
					ProxiedPlayer t = ProxyServer.getInstance().getPlayer(args[1]);
					
					if (PartyManager.isPlayerInParty(t))
					{
						p.sendMessage(Main.PARTY_PREFIX + "§7Der Spieler ist bereits in einer anderen Party!");
						return;
					}
					
					party.invitePlayer(t,p);
					p.sendMessage(Main.PARTY_PREFIX + "§7Du hast §a" + t.getName() + "§7 in deine Party eingeladen.");
					return;
				}
				else
				{
					p.sendMessage(Main.PARTY_PREFIX + "§7Du musst ein Leiter einer Party sein!");
					return;
				}
			}
			
			if (args[0].equalsIgnoreCase("join"))
			{
				/*
				 * In Entwicklung..
				 */
				p.sendMessage(Main.PARTY_PREFIX + "§7Diese Funktion wird noch programmiert..");
				return;
			}
			
			if (args[0].equalsIgnoreCase("accept"))
			{
				if (PartyManager.isPlayerInParty(p))
				{
					p.sendMessage(Main.PARTY_PREFIX + "§7Du bist bereits in einer anderen Party!");
					return;
				}
				
				Party party = PartyManager.getPartyByLeaderName(args[1]);
				party.addPlayer(p);
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
		p.sendMessage(Main.PARTY_PREFIX);
		p.sendMessage("§8>> §7/party invite <SPIELER> §8- §7Lade einen Spieler in deine Party ein.");
		p.sendMessage("§8>> §7/party join <SPIELER> §8- §7Joine einer Party.");
		p.sendMessage("§8>> §7/party accept <SPIELER> §8- §7Nehme eine Party Einladung an.");
		p.sendMessage("§8>> §7/p <NACHRICHT> §8- §7Schreibe in den PartyChat.");
		p.sendMessage("§8>> §7/party leave §8- §7Verlasse eine Party.");
		p.sendMessage("§8>> §7/party delete §8- §7Lösche die Party.");
		p.sendMessage("§8>> §7/party jump §8- §7Betrete den Server des Party Leiters.");
		p.sendMessage("§8>> §7/party list §8- §7Zeige alle Spieler in der Party.");
	}
}
