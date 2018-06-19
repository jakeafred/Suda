package dead.bot.suda.commands;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class HelpCommand extends Command {

	private TreeMap<String, Command> commands;

	public HelpCommand() {

		commands = new TreeMap<>();
	}

	public Command registerCommand(Command command) {

		commands.put(command.getCommands().get(0), command);
		return command;
	}

	@Override
	public List<String> getCommands() {

		return Arrays.asList(".help");
	}

	@Override
	public String getDescription() {

		return "Helps find information about other commands.";
	}

	@Override
	public String getName() {

		return "Help Command";
	}

	@Override
	public List<String> getInstructions() {

		return Collections.singletonList("\n.help - Lists all supported commands.\n"
				+ ".help <command> - Returns all information about that command.");
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		
		if(!e.isFromType(ChannelType.PRIVATE)) {
			
			e.getTextChannel().sendMessage(new MessageBuilder()
					.append(e.getAuthor())
					.append(" Information was sent to your DMs.")
					.build()).queue();
		}
		
		sendToPrivate(e.getAuthor().openPrivateChannel().complete(), args);
	}

	private void sendToPrivate(PrivateChannel channel, String[] args) {
		
		if (args.length < 2) {
			
			StringBuilder string = new StringBuilder();
			for (Command c : commands.values())
			{
				String description = c.getDescription();
				
				string.append("**").append(c.getCommands().get(0)).append("** - ");
				string.append(description).append("\n");
			}

			channel.sendMessage(new MessageBuilder()
					.append("**The following programs are supported by my system:**\n")
					.append("**------------------------------------------------------------------**\n\n")
					.append(string.toString()).build()).queue();
		} else {
			
			String command = args[1].charAt(0) == '.' ? args[1] : '.' + args[1];
			for (Command c : commands.values()) {
				
				if (c.getCommands().contains(command)) {
					
					String name = c.getName();
					String description = c.getDescription();
					List<String> Instructions = c.getInstructions();

					channel.sendMessage(new MessageBuilder()
							.append("**Name:** " + name + "\n")
							.append("**Description:** " + description + "\n")
							.append("**Use:**")
							.append(Instructions.get(0))
							.build()).queue();
					return;
				}
			}
			
			channel.sendMessage(new MessageBuilder()
					.append("The provided command '**" + args[1] + "**' does not exist. Use .help to list all commands.")
					.build()).queue();
		}
	}
}
