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

public class PauseCommand extends Command {

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".pause");
	}

	@Override
	public String getDescription() {
		return "Pauses the current song playing in a channel.";
	}

	@Override
	public String getName() {
		return "Pause Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n`.Pauses` - Pauses the current song playing in a channel.\n");
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
		
		if (player.getPlayingTrack() == null)
        {
            e.getChannel().sendMessage("There is no track in the queue to pause or resume.").queue();
            return;
        }

        player.setPaused(!player.isPaused());
        if (player.isPaused())
            e.getChannel().sendMessage("My player is now paused.").queue();
        else
            e.getChannel().sendMessage("My player has been resumed.").queue();

	}
}
