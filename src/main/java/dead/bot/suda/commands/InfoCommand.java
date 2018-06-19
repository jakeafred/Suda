package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import dead.bot.suda.Suda;

public class InfoCommand extends Command {

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".info", ".about");
	}

	@Override
	public String getDescription() {
		return "Shows information about my background, statistics, and functions.";
	}

	@Override
	public String getName() {
		return "Info Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n`.info` - Shows information about my background, statistics, and functions.\n");
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {

		EmbedBuilder embed = new EmbedBuilder();
		String author = "DeadAim209#5180";
		//embed.setAuthor(e.getJDA().getSelfUser().getName(), null, e.getJDA().getSelfUser().getAvatarUrl());

		StringBuilder description = new StringBuilder().append("Hello! My name is **" + e.getJDA().getSelfUser().getName() + "**!" 
				+ "\nI am a private bot coded by **" + author + "** with the intent of being a multipurpose bot, designed to perform a variety of different functions."
				+ "\nTo accomplish these tasks, I am using ").append(" [JDA](https://github.com/DV8FromTheWorld/JDA) ").append(" and [LavaPlayer](https://github.com/sedmelluq/lavaplayer)"
						+ " as imported libraries and are essential to my performance.")
				.append("\n\nMy most notable features include: ``` - Music Player with Spotify Integration \n - RPG Commands ```"
						+ "\nTo see a full list of my commands, type `.help`");
		embed.setDescription(description);
		if (e.getJDA().getShardInfo() == null)
		{
			embed.addField("Stats", e.getJDA().getGuilds().size() + " servers\n1 shard", true);
			embed.addField("Users", e.getJDA().getUsers().size() + " unique\n" + e.getJDA().getGuilds().stream().mapToInt(g -> g.getMembers().size()).sum() + " total", true);
			embed.addField("Channels", e.getJDA().getTextChannels().size() + " Text\n" + e.getJDA().getVoiceChannels().size() + " Voice", true);
		}
		else
		{
			embed.addField("Stats", (e.getJDA().getGuilds() + " Servers\nShard " + (e.getJDA().getShardInfo().getShardId() + 1) 
					+ "/" + e.getJDA().getShardInfo().getShardTotal()), true);
			embed.addField("This shard", e.getJDA().getUsers().size() + " Users\n" + e.getJDA().getGuilds().size() + " Servers", true);
			embed.addField("", e.getJDA().getTextChannels().size() + " Text Channels\n" + e.getJDA().getVoiceChannels().size() + " Voice Channels", true);
		}
		embed.setFooter("Last restart: " + Suda.uptime.getUptime() + " ago.", null);
		e.getChannel().sendMessage(embed.build()).queue();
	}
}
