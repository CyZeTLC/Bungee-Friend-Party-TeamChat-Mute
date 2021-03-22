package de.cyzetlc.cmd;

import de.cyzetlc.Main;
import de.cyzetlc.utils.Date;
import de.cyzetlc.utils.Mute;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Cmd_Mute extends Command
{
	public final String perms;
	
	public Cmd_Mute(String name, String permission, String[] aliases)
	{
		super(name, permission, aliases);
		this.perms = permission;
	}

	@Override
	public void execute(CommandSender s, String[] args)
	{
		if (!(s.hasPermission(this.perms)))
		{
			s.sendMessage(Main.PREFIX + "§cDazu hast du keine Rechte.");
			return;
		}
		
		if (args.length < 3)
		{
			s.sendMessage(Main.PREFIX + "§7Benutze §e/mute <Spieler> <Dauer ( in Stunden )> <Grund>");
			return;
		}
		
		if (Main.getPlugin().getProxy().getPlayer(args[0]) == null)
		{
			s.sendMessage(Main.PREFIX + "§cDieser Spieler ist nicht online.");		
			return;
		}
		
		ProxiedPlayer t = Main.getPlugin().getProxy().getPlayer(args[0]);
		long hours = 24;
		long till = 0;
		
		try
		{
			hours = Integer.valueOf(args[1]);
		}
		catch (NumberFormatException e)
		{
			s.sendMessage(Main.PREFIX + "§cBitte gib eine richtige Dauer an.");
			return;
		}
		
		till = System.currentTimeMillis() + (((hours * 60) * 60) * 1000);
		
		ProxiedPlayer p = (ProxiedPlayer) s;
		
		if (Mute.getMuteByUUID(t.getUniqueId().toString()) != null)
		{
			p.sendMessage(Main.PREFIX + "§cDieser Spieler ist schon gemutet.");
			return;
		}
		
		Mute m = Mute.createNewMute(t.getUniqueId().toString(), p.getUniqueId().toString(), args[2], till, true);
		
		p.sendMessage(Main.PREFIX
				+ "§7Du hast §e" + t.getName()
				+ "§7 für §e" + m.getReason()
				+ "§7 bis " + Date.getDateFormat(m.getTill())
				+ "§7 gemutet.");
		return;
	}

}
