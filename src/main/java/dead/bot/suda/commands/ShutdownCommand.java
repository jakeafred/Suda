package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.core.MessageBuilder;
import dead.bot.suda.SettingsManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ShutdownCommand extends Command {

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".shutdown");
	}

	@Override
	public String getDescription() {
		return "Initiates my internal shutdown.";
	}

	@Override
	public String getName() {
		return "Shutdown Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n`.shutdown` - Initiates my internal shutdown.");
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		String author = e.getAuthor().getId();
		String owner = SettingsManager.getInstance().getSettings().getOwner();
		if (e.getAuthor().isBot()) {
			return;
		} 
		if (!author.contains(owner)) {
			e.getChannel().sendMessage(new MessageBuilder().append("You do not have permission to use this command.").build()).queue();
			return;
		}
		e.getChannel().sendMessage(new MessageBuilder().append("Shutting down...").build()).queue();
		System.exit(0);
	}
}
