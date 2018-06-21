package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;

import dead.bot.suda.player.SudaManager;
import dead.bot.suda.player.TrackScheduler;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SkipCommand extends Command {

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".skip");
	}

	@Override
	public String getDescription() {
		return "Skips the current song.";
	}

	@Override
	public String getName() {
		return "Skip Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n`.skip` - Skips the current song and stops my player if queue is empty.\n");
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if (e.isFromType(ChannelType.PRIVATE)) {
			PrivateChannel pchannel = e.getPrivateChannel();
			pchannel.sendMessage(new MessageBuilder()
					.append("You cannot use this command here. Please try again in a public text channel")
					.build()).queue();
			return;
		} if (e.getMember().getVoiceState().getChannel() == null) {
			e.getTextChannel().sendMessage(new MessageBuilder()
					.append(e.getAuthor())
					.append(" You are not in a voice channel. Please join a voice channel and try again.")
					.build()).queue();
			return;
		}
		
		Guild guild = e.getGuild();
		SudaManager manager = SudaManager.getMusicManager(guild);
		TrackScheduler scheduler = manager.scheduler;
		
		if (scheduler.queue.isEmpty()) {
			e.getTextChannel().sendMessage(new MessageBuilder().append("There is nothing in the queue to skip to!").build()).queue();
		} else {
			scheduler.nextTrack();
			e.getTextChannel().sendMessage(new MessageBuilder().append("The current song has been skipped.").build()).queue();
		}
	}
}
