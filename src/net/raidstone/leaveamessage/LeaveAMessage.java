package net.raidstone.leaveamessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author weby@we-bb.com [Nicolas Glassey]
 * @version 1.0.0
 * @since 11/14/16
 */
public class LeaveAMessage extends JavaPlugin implements Listener
{
    String message = null;
    @Override
    public void onEnable()
    {
        String m = this.getConfig().getString("message");
        if(m!=null && !m.equalsIgnoreCase(""))
            message = ChatColor.translateAlternateColorCodes('&',m);
    
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    
    @Override
    public void onDisable()
    {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,String label, String[] args)
    {
        String n = cmd.getName().toLowerCase();
        
        if(!n.equals("leaveamessage"))
            return true;
       
        // Disable any previous message
        if(args.length<1)
        {
            this.getConfig().set("message", null);
            this.saveConfig();
            sender.sendMessage("The message has been cleared !");
            message=null;
            return true;
        }
        
        message = "";
        
        for(int i = 0; i<args.length; i++)
        {
            message+=args[i]+" ";
        }
        message = ChatColor.translateAlternateColorCodes('&', message);
       
        sender.sendMessage("--------------------------");
        sender.sendMessage("Message has been set to :");
        sender.sendMessage(message);
        sender.sendMessage("--------------------------");
        
        this.getConfig().set("message", message);
        this.saveConfig();
        
        return true;
    }
    
    @EventHandler
    public void onPlayerLogin(final PlayerJoinEvent event)
    {
        if(message==null) return;
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(event.getPlayer()!=null && event.getPlayer().isOnline())
                {
                    event.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Server message :");
                    event.getPlayer().sendMessage(ChatColor.DARK_AQUA + message);
                }
            }
        }.runTaskLater(this, 40L);
    }
}
