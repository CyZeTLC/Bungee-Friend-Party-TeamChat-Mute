package de.cyzetlc.cmd;

import de.cyzetlc.Main;
import de.cyzetlc.utils.Mute;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Cmd_Unmute extends Command
{
	public final String perms;
	
	public Cmd_Unmute(String name, String permission, String[] aliases)
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
		
		if (args.length < 1)
		{
			s.sendMessage(Main.PREFIX + "§7Benutze §e/unmute <Spieler>");
			return;
		}
		
		if (Main.getPlugin().getProxy().getPlayer(args[0]) == null)
		{
			s.sendMessage(Main.PREFIX + "§cDieser Spieler ist nicht online.");		
			return;
		}
		
		ProxiedPlayer t = Main.getPlugin().getProxy().getPlayer(args[0]);
		
		ProxiedPlayer p = (ProxiedPlayer) s;
		
		if (Mute.getMuteByUUID(t.getUniqueId().toString()) == null)
		{
			p.sendMessage(Main.PREFIX + "§cDieser Spieler ist nicht gemutet.");
			return;
		}
		
		Mute m = Mute.getMuteByUUID(t.getUniqueId().toString());
		
		Mute.removeMuteByUUID(t.getUniqueId().toString());
		Main.mutes.remove(m);
		Main.mutes.remove(m);
		
		p.sendMessage(Main.PREFIX + "§7Du hast §e" + t.getName() + "§7 unmutet.");
		return;
	}

}
