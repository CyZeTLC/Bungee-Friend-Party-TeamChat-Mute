package de.cyzetlc.utils.manager.friends;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import de.cyzetlc.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendManager
{  
  public static List<String> getFriends(String uuid)
  {
      List<String> friends = new ArrayList<String>();

      try
      {
        ResultSet rs = Main.mySQL.query("SELECT * FROM friend WHERE UUID = '" + uuid + "'");
        while (rs.next()) 
        {
          friends.add(rs.getString("friendName"));
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    return friends;
  }
   
  public static void addFriend(final String uuid, String uuidFriend, String friendName)
  {
      Thread thread = new Thread()
      {
        public void run()
        {
          Main.mySQL.update("INSERT INTO friend (UUID, uuidFriend, friendName) VALUES ('" + uuid + "', '"+ uuidFriend + "', '" + friendName + "');");
        }
      };
      thread.setPriority(1);
      thread.start();
  }
  
  public static void removeFriend(final String uuid, String uuidFriend, String friendName)
  {
      Thread thread = new Thread()
      {
        public void run()
        {
          Main.mySQL.update("DELETE FROM friend WHERE UUID = '"+uuid+"' AND uuidFriend = '"+uuidFriend+"';");
        }
      };
      thread.setPriority(1);
      thread.start();
  }
  
  public static boolean isPlayerHaveFriend(ProxiedPlayer user, String friendName)
  {
	  if (getFriends(user.getUniqueId().toString()).contains(friendName))
	  {
		  return true;
	  }
	  return false;
  }
  
  public static List<String> getInvites(String uuid)
  {
      List<String> friends = new ArrayList<String>();

      try
      {
        ResultSet rs = Main.mySQL.query("SELECT * FROM friendInvites WHERE UUID = '" + uuid + "'");
        while (rs.next()) 
        {
          friends.add(rs.getString("friendName"));
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    return friends;
  }
   
  public static void addInvite(final String uuid, String name, String uuidFriend, String friendName)
  {
      Thread thread = new Thread()
      {
        public void run()
        {
          Main.mySQL.update("INSERT INTO friendInvites (UUID, name, uuidFriend, friendName) VALUES ('" + uuid + "', '"+name+"', '" + uuidFriend + "', '" + friendName + "');");
        }
      };
      thread.setPriority(1);
      thread.start();
  }
  
  public static boolean isPlayerHaveInvite(ProxiedPlayer user, String friendName)
  {
	  if (getInvites(user.getUniqueId().toString()).contains(friendName))
	  {
		  return true;
	  }
	  return false;
  }
  
  public static void removeInvite(final String uuid, String uuidFriend, String friendName)
  {
      Thread thread = new Thread()
      {
        public void run()
        {
          Main.mySQL.update("DELETE FROM friendInvites WHERE UUID = '"+uuid+"' AND uuidFriend = '"+uuidFriend+"';");
        }
      };
      thread.setPriority(1);
      thread.start();
  }
  
  public static void inviteFriend(ProxiedPlayer p, String uuidFriend, String friendName)
  {
	  addInvite(uuidFriend, friendName, p.getUniqueId().toString(), p.getName());
	  
	  if (ProxyServer.getInstance().getPlayer(friendName) != null)
	  {
		  ProxiedPlayer t = ProxyServer.getInstance().getPlayer(friendName);
		  
	      TextComponent textComponent = new TextComponent();
	      textComponent.setText("§7[§aANNEHMEN§7]");
	      textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend accept " + p.getName()));
	      
	      TextComponent textComponent1 = new TextComponent();
	      textComponent1.setText("§7[§cABLEHNEN§7]");
	      textComponent1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friend deny " + p.getName()));
	      
	      t.sendMessage(Main.PARTY_PREFIX + "§7Der Spieler §a" + p.getName() + "§7 möchte dich als Freund haben.");
	      t.sendMessage(textComponent);
	      t.sendMessage(textComponent1);
	  }
      return;
  }
}
