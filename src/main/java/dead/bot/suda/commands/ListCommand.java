package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dead.bot.suda.player.SudaManager;
import dead.bot.suda.player.TrackScheduler;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ListCommand extends Command {

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".list", ".queue");
	}

	@Override
	public String getDescription() {
		return "Lists all of the current songs within my player's queue.";
	}

	@Override
	public String getName() {
		return "List Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n.list - Lists all of the current songs within my player's queue.\n");
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
		TrackScheduler scheduler = manager.scheduler;
		Queue<AudioTrack> queue = scheduler.queue;
		synchronized (queue) {
			if (queue.isEmpty()) {
				e.getChannel().sendMessage("The queue is currently empty!").queue();
			} else {
				int trackCount = 0;
				long queueLength = 0;
				StringBuilder sb = new StringBuilder();
				sb.append("**__Current Tracks in Queue: ").append(queue.size()).append("__**\n\n");
				for (AudioTrack track : queue) {
					queueLength += track.getDuration();
					if (trackCount < 10) {
						sb.append("`[").append(SudaManager.getTimestamp(track.getDuration())).append("]` ");
						sb.append(track.getInfo().title).append("\n");
						trackCount++;
					}
				}
				sb.append("\n").append("**Total Queue Time: ").append(SudaManager.getTimestamp(queueLength)).append("**");

				e.getChannel().sendMessage(sb.toString()).queue();
			}
		}
	}
}
