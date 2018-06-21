package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import dead.bot.suda.player.SudaManager;
import dead.bot.suda.player.SudaPlayer;
import dead.bot.suda.player.TrackScheduler;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

public class PlayCommand extends Command {

	private String msg;
	
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
		return Arrays.asList("\n`.play` - Resumes my player if it was stopped.\n"
				+ "`.play <url>` - Plays the song at the selected URL.\n"
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

		VoiceChannel channel = e.getMember().getVoiceState().getChannel();
		Guild guild = e.getGuild();
		SudaManager manager = SudaManager.getMusicManager(guild);
		AudioPlayer player = manager.player;
		TrackScheduler scheduler = manager.scheduler;

		if (args.length == 1)
		{
			if (player.isPaused())
			{
				player.setPaused(false);
				e.getChannel().sendMessage("My player has been resumed.").queue();
			}
			else if (player.getPlayingTrack() != null)
			{
				e.getChannel().sendMessage("My player is currently online!").queue();
			}
			else if (scheduler.queue.isEmpty())
			{
				e.getChannel().sendMessage("The current audio queue is empty! Add something to the queue first!").queue();
			}
		} else if (args.length == 2) {
			guild.getAudioManager().setSendingHandler(manager.sendHandler);

			try {
				guild.getAudioManager().openAudioConnection(e.getMember().getVoiceState().getChannel());
			} catch (PermissionException exception) {
				if (exception.getPermission() == Permission.VOICE_CONNECT) {
					e.getChannel().sendMessage("I do not have permission to connect to: " + channel.getName()).queue();
				}
			}
			playSong(manager, e.getChannel(), args[1]);
		}
	}

	private void playSong(SudaManager mng, final MessageChannel channel, String url) {
		final String trackUrl;

		//Strip <>'s that prevent discord from embedding link resources
		if (url.startsWith("<") && url.endsWith(">")) {
			trackUrl = url.substring(1, url.length() - 1);
		} else {
			trackUrl = url;
		}

		SudaPlayer.playerManager.loadItemOrdered(mng, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				if (mng.player.getPlayingTrack() == null) {
				msg = "Adding **" + track.getInfo().title + "** to the queue and starting my player.";
				} else {
					msg = "Adding **" + track.getInfo().title + "** to the queue.";
				}

				mng.scheduler.queue(track);
				channel.sendMessage(msg).queue();
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				if (url.contains("list")) {
					List<AudioTrack> tracks = playlist.getTracks();

					channel.sendMessage("Adding **" + playlist.getTracks().size() +"** tracks to the queue from **" + playlist.getName() + "**").queue();
					tracks.forEach(mng.scheduler::queue);
				}
			}

			@Override
			public void noMatches() {
				channel.sendMessage("Invalid URL: " + trackUrl /*+ ". Please select one of the following tracks:"*/).queue();
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Could not play: " + exception.getMessage()).queue();
			}
		});
	}
}