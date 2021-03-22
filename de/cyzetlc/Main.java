package de.cyzetlc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.cyzetlc.cmd.Cmd_Friend;
import de.cyzetlc.cmd.Cmd_Mute;
import de.cyzetlc.cmd.Cmd_PChat;
import de.cyzetlc.cmd.Cmd_Party;
import de.cyzetlc.cmd.Cmd_Unmute;
import de.cyzetlc.handler.ChatHandler;
import de.cyzetlc.handler.TeamChat;
import de.cyzetlc.handler.friend.FriendHandler;
import de.cyzetlc.handler.party.PartyHandler;
import de.cyzetlc.utils.Mute;
import de.cyzetlc.utils.manager.party.PartyManager;
import de.cyzetlc.utils.mysql.MySQL;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin
{
	public static final String PREFIX = "§7» §b§lBungee §8§l�? §r";
	public static final String PARTY_PREFIX = "§7» §b§lParty §8§l�? §r";
	public static final String FRIEND_PREFIX = "§7» §b§lFreunde §8§l�? §r";

	public static final List<Mute> mutes = new ArrayList<>();
	
	public static MySQL mySQL;
	public static Main plugin;
	
	public void onEnable() 
	{
		this.loadBlocked();
		this.loadConfig();
		this.getProxy().getPluginManager().registerListener(this, new ChatHandler());
		this.getProxy().getPluginManager().registerCommand(this, new Cmd_Mute("mute", "system.mute", new String[] {}));
		this.getProxy().getPluginManager().registerCommand(this, new Cmd_Unmute("unmute", "system.mute", new String[] {}));
		this.getProxy().getPluginManager().registerCommand(this, new Cmd_Party());
		this.getProxy().getPluginManager().registerCommand(this, new Cmd_PChat());
		this.getProxy().getPluginManager().registerCommand(this, new Cmd_Friend());
		this.getProxy().getPluginManager().registerListener(this, new PartyHandler());
		this.getProxy().getPluginManager().registerListener(this, new FriendHandler());
		this.getProxy().getPluginManager().registerListener(this, new TeamChat());
		
		new PartyManager();

		this.plugin = this;
		try { this.mySQL = MySQL.initMySQL(); } catch (IOException e) { e.printStackTrace(); }
		
		this.mySQL.update("CREATE TABLE IF NOT EXISTS friendInvites (UUID VARCHAR(100), name VARCHAR(100), uuidFriend VARCHAR(100), friendName VARCHAR(100));");
		this.mySQL.update("CREATE TABLE IF NOT EXISTS friend (UUID VARCHAR(100), uuidFriend VARCHAR(100), friendName VARCHAR(100));");
	}
	
	private void loadConfig()
	{
		try
		{
			File f = new File(this.getDataFolder(), "mutes.yml");
			
			if (!f.exists())
			{
				f.createNewFile();
			}
			
	        Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
			
			for (String str : cfg.getKeys())
			{
				this.mutes.add(Mute.createNewMute(cfg.getString(str+".uuid"), cfg.getString(str+".uuidAdmin"), cfg.getString(str+".reason"), cfg.getLong(str+".till"), false));
			}
			
	        ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, f);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	private void loadBlocked()
	{
		try
		{
			File f = new File(this.getDataFolder(), "blocks.txt");
			
			if (!f.exists())
				f.createNewFile();
			
			Scanner sc = new Scanner(f, "UTF-8");
			
			while (sc.hasNextLine())
			{
				ChatHandler.blocked.add(sc.nextLine().toLowerCase());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static Main getPlugin()
	{
		return plugin;
	}
}
