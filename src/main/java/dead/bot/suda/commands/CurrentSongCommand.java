package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import dead.bot.suda.player.SudaManager;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CurrentSongCommand extends Command {

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".currentsong", ".nowplaying");
	}

	@Override
	public String getDescription() {
		return "Identifies the current song being played.";
	}

	@Override
	public String getName() {
		return "CurrentSong Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n`.currentsong` - Identifies the current song being played.\n");
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
		
		AudioTrack currentTrack = player.getPlayingTrack();
        if (currentTrack != null)
        {
            String title = currentTrack.getInfo().title;
            String position = SudaManager.getTimestamp(currentTrack.getPosition());
            String duration = SudaManager.getTimestamp(currentTrack.getDuration());

            String nowplaying = String.format("**Playing:** %s\n**Time:** [%s / %s]",
                    title, position, duration);

            e.getChannel().sendMessage(nowplaying).queue();
        }
        else
            e.getChannel().sendMessage("The player is not currently playing anything!").queue();
	}
}
