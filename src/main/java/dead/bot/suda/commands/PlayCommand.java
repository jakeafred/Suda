package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import dead.bot.suda.player.SudaManager;
import dead.bot.suda.player.SudaPlayer;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class PlayCommand extends Command {

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".play");
	}

	@Override
	public String getDescription() {
		return "Plays queued music or resumes my player.";
	}

	@Override
	public String getName() {
		return "Play Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n`.play` - Resumes my player if it was Stopped.\n"
				+ "`.play <url>` - Plays the Song at the Selected URL.\n"
				+ "`.play <keywords>` - Searches for songs using the given keywords.");
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if (e.isFromType(ChannelType.PRIVATE)) {
			PrivateChannel pchannel = e.getPrivateChannel();
			pchannel.sendMessage(new MessageBuilder()
					.append("You cannot use this command here. Please try again in a public text channel")
					.build()).queue();
			return;
		}
		if (e.getMember().getVoiceState().getChannel() == null) {
			e.getTextChannel().sendMessage(new MessageBuilder()
					.append(e.getAuthor())
					.append(" You are not in a voice channel. Please join a voice channel and try again.")
					.build()).queue();
			return;
		}
	}
}
