package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import dead.bot.suda.player.SudaManager;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class VolumeCommand extends Command {

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".volume");
	}

	@Override
	public String getDescription() {
		return "Changes the volume of my player to the desired number (0-100).";
	}

	@Override
	public String getName() {
		return "Volume Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n.volume <command> - Changes the volume of my player to the desired number (0-100).\n");
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if (e.isFromType(ChannelType.PRIVATE)) {
			PrivateChannel pchannel = e.getPrivateChannel();
			pchannel.sendMessage(new MessageBuilder()
					.append("You cannot use this command here. Please try again in a public text channel")
					.build()).queue();
			return;
		} else if (e.getMember().getVoiceState().getChannel() == null) {
			e.getTextChannel().sendMessage(new MessageBuilder()
					.append(e.getAuthor())
					.append(" You are not in a voice channel. Please join a voice channel and try again.")
					.build()).queue();
			return;
		}
		Guild guild = e.getGuild();
		SudaManager manager = SudaManager.getMusicManager(guild);
		AudioPlayer player = manager.player;
		
		 if (args.length == 1)
         {
             e.getChannel().sendMessage("Current volume: **" + player.getVolume() + "**").queue();
         }
         else
         {
             try
             {
                 int newVolume = Math.max(0, Math.min(100, Integer.parseInt(args[1])));
                 int oldVolume = player.getVolume();
                 player.setVolume(newVolume);
                 e.getChannel().sendMessage("Player volume changed from `" + oldVolume + "` to `" + newVolume + "`").queue();
             }
             catch (NumberFormatException exception)
             {
                 e.getChannel().sendMessage("`" + args[1] + "` is not a valid integer. (10 - 100)").queue();
             }
         }

	}
}
