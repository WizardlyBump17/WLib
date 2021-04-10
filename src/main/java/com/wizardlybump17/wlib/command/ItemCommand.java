package com.wizardlybump17.wlib.command;

import com.google.gson.Gson;
import com.wizardlybump17.wlib.reflection.ItemAdapter;
import com.wizardlybump17.wlib.reflection.ReflectionAdapter;
import com.wizardlybump17.wlib.reflection.ReflectionAdapterRegister;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class ItemCommand implements CommandExecutor {

    // /item set <type> <key> <tag>
    // /item get <key>

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ReflectionAdapter reflectionAdapter = ReflectionAdapterRegister.getInstance().getServerAdapter();
        Player player = (Player) sender;
        ItemStack item = player.getItemInHand();
        ItemAdapter itemAdapter = reflectionAdapter.getItemAdapter(item);
        switch (args[0].toLowerCase()) {
            case "set": {
                String type = args[1];
                String key = args[2];
                Object tag = StringUtils.join(args, ' ', 3, args.length);
                switch (type.toLowerCase()) {
                    case "byte": {
                        tag = Byte.parseByte(tag.toString());
                        break;
                    }
                    case "byte[]": {
                        byte[] bytes = new byte[tag.toString().split(" ").length];
                        int index = 0;
                        for (String s : tag.toString().split(" "))
                            bytes[index++] = Byte.parseByte(s);
                        tag = bytes;
                        break;
                    }
                    case "short": {
                        tag = Short.parseShort(tag.toString());
                        break;
                    }
                    case "int": {
                        tag = Integer.parseInt(tag.toString());
                        break;
                    }
                    case "int[]": {
                        int[] ints = new int[tag.toString().split(" ").length];
                        int index = 0;
                        for (String s : tag.toString().split(" "))
                            ints[index++] = Integer.parseInt(s);
                        tag = ints;
                        break;
                    }
                    case "long":{
                        tag = Long.parseLong(tag.toString());
                        break;
                    }
                    case "float": {
                        tag = Float.parseFloat(tag.toString());
                        break;
                    }
                    case "double": {
                        tag = Double.parseDouble(tag.toString());
                        break;
                    }
                    case "list": {
                        tag = new Gson().fromJson(tag.toString(), List.class);
                        break;
                    }
                    case "map": {
                        tag = new Gson().fromJson(tag.toString(), Map.class);
                        break;
                    }
                }
                itemAdapter.setNbtTag(key, tag);
                player.setItemInHand(itemAdapter.getTarget());
                return true;
            }

            case "get": {
                player.sendMessage(itemAdapter.getNbtTag(args[1].toLowerCase()).toString());
                return true;
            }
        }
        return true;
    }
}
