
package me.Hoot215.PermaWeather;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PermaWeather extends JavaPlugin implements Listener
  {
    private Logger logger = null;
    
    public void logInfo (String message)
      {
        this.logger.info(message);
      }
    
    public void logWarning (String message)
      {
        this.logger.warning(message);
      }
    
    public void logSevere (String message)
      {
        this.logger.severe(message);
      }
    
    public File getFile (World world)
      {
        File dataFolder = new File(this.getDataFolder(), "data");
        if ( !dataFolder.exists())
          {
            dataFolder.mkdir();
          }
        return new File(dataFolder, world.getName());
      }
    
    public boolean isStormy (World world)
      {
        File file = this.getFile(world);
        return file.exists();
      }
    
    public boolean setSun (World world)
      {
        if (this.isStormy(world))
          {
            world.setStorm(false);
            world.setWeatherDuration(0);
            File file = this.getFile(world);
            file.delete();
            return true;
          }
        return false;
      }
    
    public boolean setStorm (World world)
      {
        if ( !this.isStormy(world))
          {
            world.setStorm(true);
            world.setWeatherDuration(51840000);
            File file = this.getFile(world);
            try
              {
                file.createNewFile();
              }
            catch (IOException e)
              {
                e.printStackTrace();
              }
            return true;
          }
        return false;
      }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onWorldLoad (WorldLoadEvent event)
      {
        World world = event.getWorld();
        if (this.isStormy(world))
          {
            this.setStorm(world);
          }
      }
    
    public boolean onCommand (CommandSender sender, Command cmd,
      String commandLabel, String[] args)
      {
        if (cmd.getName().equalsIgnoreCase("permaweather"))
          {
            if (sender.hasPermission(cmd.getPermission()))
              {
                if (args.length == 0)
                  {
                    return false;
                  }
                if (args.length == 1)
                  {
                    if ( ! (sender instanceof Player))
                      {
                        sender.sendMessage(ChatColor.RED + "This command can"
                            + " only be run by a player!");
                        return true;
                      }
                    Player player = (Player) sender;
                    World world = player.getWorld();
                    String value = args[0];
                    if (value.equalsIgnoreCase("sun")
                        || value.equalsIgnoreCase("sunny"))
                      {
                        if (this.setSun(world))
                          {
                            player.sendMessage(ChatColor.GREEN
                                + "Weather set to " + ChatColor.YELLOW + value
                                + ChatColor.GREEN + " in world "
                                + ChatColor.LIGHT_PURPLE + world.getName());
                          }
                        else
                          {
                            player.sendMessage(ChatColor.RED
                                + "The weather is already " + value
                                + " in world " + world.getName());
                          }
                        return true;
                      }
                    if (value.equalsIgnoreCase("storm")
                        || value.equalsIgnoreCase("stormy")
                        || value.equalsIgnoreCase("rain")
                        || value.equalsIgnoreCase("rainy")
                        || value.equalsIgnoreCase("snow")
                        || value.equalsIgnoreCase("snowy"))
                      {
                        if (this.setStorm(world))
                          {
                            player.sendMessage(ChatColor.GREEN
                                + "Weather set to " + ChatColor.YELLOW + value
                                + ChatColor.GREEN + " in world "
                                + ChatColor.LIGHT_PURPLE + world.getName());
                          }
                        else
                          {
                            player.sendMessage(ChatColor.RED
                                + "The weather is already " + value
                                + " in world " + world.getName());
                          }
                        return true;
                      }
                    player
                        .sendMessage(ChatColor.RED + "Unknown weather value!");
                    return false;
                  }
                if (args.length == 2)
                  {
                    String worldName = args[0];
                    String value = args[1];
                    World world = this.getServer().getWorld(worldName);
                    if (world == null)
                      {
                        sender.sendMessage(ChatColor.RED + "Unknown world!");
                        return false;
                      }
                    if (value.equalsIgnoreCase("sun")
                        || value.equalsIgnoreCase("sunny"))
                      {
                        if (this.setSun(world))
                          {
                            sender.sendMessage(ChatColor.GREEN
                                + "Weather set to " + ChatColor.YELLOW + value
                                + ChatColor.GREEN + " in world "
                                + ChatColor.LIGHT_PURPLE + worldName);
                          }
                        else
                          {
                            sender.sendMessage(ChatColor.RED
                                + "The weather is already " + value
                                + " in world " + worldName);
                          }
                        return true;
                      }
                    if (value.equalsIgnoreCase("storm")
                        || value.equalsIgnoreCase("stormy")
                        || value.equalsIgnoreCase("snow")
                        || value.equalsIgnoreCase("snowy"))
                      {
                        if (this.setStorm(world))
                          {
                            sender.sendMessage(ChatColor.GREEN
                                + "Weather set to " + ChatColor.YELLOW + value
                                + ChatColor.GREEN + " in world "
                                + ChatColor.LIGHT_PURPLE + worldName);
                          }
                        else
                          {
                            sender.sendMessage(ChatColor.RED
                                + "The weather is already " + value
                                + " in world " + worldName);
                          }
                      }
                    sender
                        .sendMessage(ChatColor.RED + "Unknown weather value!");
                    return false;
                  }
                sender.sendMessage(ChatColor.RED + "Too many arguements!");
                return false;
              }
            sender.sendMessage(cmd.getPermissionMessage());
            return true;
          }
        return false;
      }
    
    @Override
    public void onDisable ()
      {
        this.logInfo("Is now disabled!");
      }
    
    @Override
    public void onEnable ()
      {
        logger = this.getLogger();
        this.getServer().getPluginManager().registerEvents(this, this);
        this.logInfo("Is now enabled!");
      }
  }
