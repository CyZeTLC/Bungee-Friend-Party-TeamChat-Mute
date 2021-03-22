package de.cyzetlc.utils.party;

import java.util.ArrayList;
import java.util.List;

import de.cyzetlc.Main;
import de.cyzetlc.utils.manager.party.PartyManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Party 
{
	private ProxiedPlayer owner;
	private List<ProxiedPlayer> players;
	
	public Party(ProxiedPlayer owner)
	{
		this.owner = owner;
		this.players = new ArrayList<>();
		this.players.add(owner);
		
		PartyManager.addParty(this);
	}
	
	public void setOwner(ProxiedPlayer owner) 
	{
		this.owner = owner;
	}

	public void addPlayer(ProxiedPlayer p)
	{
		if (PartyManager.getInquirys().containsKey(p) && PartyManager.getInquirys().get(p).contains(this))
		{
			this.players.add(p);
			this.alert("Der Spieler §e" + p.getName() + "§7 ist der Party §abeigetreten§7.");
		}
		else
		{
			p.sendMessage(Main.PARTY_PREFIX + "§7Du hast von dieser Party keine Einladung bekommen!");
		}
	}
	
	public void removePlayer(ProxiedPlayer p)
	{
		this.players.remove(p);
		this.alert("Der Spieler §e" + p.getName() + "§7 hat die Party §cverlassen§7.");
		
		if (this.getPlayers().size() >= 1)
		{
			if (this.isOwner(p))
			{
				this.setOwner(this.getPlayers().get(0));
				this.alert("§a" + this.owner.getName() + "§7 ist nun der Party Leiter.");
			}
		}
		else
		{
			this.delete();
		}
	}
	
	public boolean isOwner(ProxiedPlayer p)
	{
		if (this.getOwner().equals(p))
		{
			return true;
		}
		return false;
	}
	
	public ProxiedPlayer getOwner() 
	{
		return owner;
	}
	
	public List<ProxiedPlayer> getPlayers() 
	{
		return players;
	}
	
	public void invitePlayer(ProxiedPlayer p, ProxiedPlayer s)
	{		
		if (!PartyManager.getInquirys().containsKey(p))
		{
			PartyManager.getInquirys().put(p, new ArrayList<>());
		}
		
		List<Party> invites = PartyManager.getInquirys().get(p);
		
		if (invites.contains(this))
		{
			s.sendMessage(Main.PARTY_PREFIX + "§7Du hast diesen Spieler bereits eingeladen!");
			return;
		}
		
		invites.add(this);
		
        TextComponent textComponent = new TextComponent();
        textComponent.setText(Main.PARTY_PREFIX + "§7Du wurdest von §a" + this.getOwner().getName() + "§7 in eine Party eingeladen.");
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + this.getOwner().getName()));
		
		p.sendMessage(textComponent);
		PartyManager.getInquirys().put(p, invites);
	}
	
	public void delete()
	{
		this.alert("§cDie Party wurde aufgelöst!");
		this.players.clear();
		PartyManager.removeParty(this);
	}
	
	public void alert(Object msg)
	{
		for (ProxiedPlayer all : players)
		{
			all.sendMessage(Main.PARTY_PREFIX + "§7" + msg);
		}
	}
	
	public void sendMessageFromPlayer(ProxiedPlayer sender, Object msg)
	{
		for (ProxiedPlayer all : players)
		{
			all.sendMessage(Main.PARTY_PREFIX + "§a" + sender.getName() + "§8: §7" + msg);
		}
	}
}
