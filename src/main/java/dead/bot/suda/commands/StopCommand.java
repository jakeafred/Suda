package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import dead.bot.suda.player.SudaManager;
import dead.bot.suda.player.TrackScheduler;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StopCommand extends Command {

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".stop");
	}

	@Override
	public String getDescription() {
		return "Stops the player and clears my player's queue.";
	}

	@Override
	public String getName() {
		return "Stop Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n.stop - Stops the player and clears my player's queue.\n");
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
		TrackScheduler scheduler = manager.scheduler;
		
		scheduler.queue.clear();
        player.stopTrack();
        player.setPaused(false);
        guild.getAudioManager().setSendingHandler(null);
        guild.getAudioManager().closeAudioConnection();
        e.getChannel().sendMessage("My player has been stopped and my queue is now empty.").queue();
	}
}