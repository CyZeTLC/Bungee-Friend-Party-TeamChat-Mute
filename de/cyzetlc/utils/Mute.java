package de.cyzetlc.utils;

import java.io.File;

import de.cyzetlc.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Mute 
{
	final String uuid;
	final String uuidAdmin;
	final String reason;
	final long till;
	
	private Mute(String uuid, String uuidAdmin, String reason, long till, boolean save)
	{
		this.uuid = uuid;
		this.uuidAdmin = uuidAdmin;
		this.reason = reason;
		this.till = till;

		if (save)
		{
			this.saveToConfig();
		}
		
		Main.mutes.add(this);
	}
	
	public static Mute getMuteByUUID(String uuid)
	{
		Mute m = null;
		
		for (Mute mute : Main.mutes)
		{
			if (mute.getUuid().equals(uuid))
			{
				m = mute;
				break;
			}
		}
		return m;
	}
	
	public static void removeMuteByUUID(String uuid)
	{
		Mute m = getMuteByUUID(uuid);
		m.removeMute();
	}
	
	public static Mute createNewMute(String uuid, String uuidAdmin, String reason, long till, boolean save)
	{
		return new Mute(uuid, uuidAdmin, reason, till, save);
	}
	
	public void removeMute()
	{
		try
		{
			File f = new File(Main.getPlugin().getDataFolder(), "mutes.yml");
	        Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
	        
	        cfg.set(this.uuid+".till", 0);
	        	
	        ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, f);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void saveToConfig()
	{
		try
		{
			File f = new File(Main.getPlugin().getDataFolder(), "mutes.yml");
	        Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
	        
	        cfg.set(this.uuid+".uuid", this.uuid);
	        cfg.set(this.uuid+".uuidAdmin", this.uuidAdmin);
	        cfg.set(this.uuid+".reason", this.reason);
	        cfg.set(this.uuid+".till", this.till);
	        	
	        ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, f);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String getReason() 
	{
		return reason;
	}
	
	public long getTill() 
	{
		return till;
	}
	
	public String getUuid() 
	{
		return uuid;
	}
	
	public String getUuidAdmin() 
	{
		return uuidAdmin;
	}
}
