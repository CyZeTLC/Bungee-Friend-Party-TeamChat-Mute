package de.cyzetlc.utils.manager.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.cyzetlc.utils.party.Party;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyManager
{
	private static List<Party> partys;
	private static HashMap<ProxiedPlayer, List<Party>> inquirys;
	
	public PartyManager()
	{
		this.partys = new ArrayList<>();
		this.inquirys = new HashMap<>();
	}
	
	/*
	 * Static Getters
	 */
	public static boolean isPlayerInParty(ProxiedPlayer p)
	{
		for (Party party : getPartys())
		{
			if (party.getPlayers().contains(p))
			{
				return true;
			}
		}
		return false;
	}
	
	public static Party getPartyOfPlayer(ProxiedPlayer p)
	{
		for (Party party : getPartys())
		{
			if (party.getPlayers().contains(p))
			{
				return party;
			}
		}
		return null;
	}
	
	public static Party getPartyByLeaderName(String name)
	{
		for (Party party : getPartys())
		{
			if (party.getOwner().getName().equalsIgnoreCase(name))
			{
				return party;
			}
		}
		return null;
	}
	
	public static boolean isPartyLeader(ProxiedPlayer p)
	{
		if (isPlayerInParty(p) && getPartyOfPlayer(p).isOwner(p))
		{
			return true;
		}
		return false;
	}
	
	public static void addParty(Party party)
	{
		getPartys().add(party);
	}
	
	public static void removeParty(Party party)
	{
		getPartys().remove(party);
	}
	
	public static HashMap<ProxiedPlayer, List<Party>> getInquirys() 
	{
		return inquirys;
	}
	
	public static List<Party> getPartys() 
	{
		return partys;
	}
	
	public static String getServiceName(ServerInfo server)
	{
		return server.getName().split("-")[0];
	}
}
