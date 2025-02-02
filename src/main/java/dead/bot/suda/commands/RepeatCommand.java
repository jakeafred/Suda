package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RepeatCommand extends Command {

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".repeat");
	}

	@Override
	public String getDescription() {
		return "Sets the player to repeat the last played song.";
	}

	@Override
	public String getName() {
		return "Play Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n`.repeat` - Sets the player to repeat the last played song.");
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
		e.getTextChannel().sendMessage(new MessageBuilder().append("Player has been put on repeat.").build()).queue();

	}
}
