package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SessionCommand extends Command {
	
	private int sescount = 0;
	private int sesmax = 6;
	private boolean session = false;
	private String[] players = {"-", "-", "-", "-", "-", "-"};
	private String creator = "-";

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".session");
	}

	@Override
	public String getDescription() {
		return "Creates/Deletes a session, Joins/Leaves a session, or List a session's Players";
	}

	@Override
	public String getName() {
		return "Session Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n`.session create` - Creates a new session if one is not active.\n"
				+ "`.session join` - Joins a session if one is active.\n"
				+ "`.session leave` - Leaves a session if you are signed up for one.\n"
				+ "`.session delete` - Deletes an active session.\n"
				+ "`.session list` - Lists the people in the session\n");
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if (args.length < 2) {
			e.getTextChannel().sendMessage(new MessageBuilder().append("Please identify what you want to do with this command and try again.\n") 
					.append("Possibilities: Create, Join, Leave, Delete, List").build()).queue();
			return;
		} else {
			//Create Command
			if (args[1].contains("create")) {
				if (session == false) {
					session = true;
					creator = e.getAuthor().getId();
					e.getTextChannel().sendMessage(new MessageBuilder().append("Session successfully created by " + e.getMember().getNickname() + ".").build()).queue();
					return;
				} else {
					e.getTextChannel().sendMessage(new MessageBuilder().append("There is already an existing session going on.").build()).queue();
					return;
				}
			}
			//Join Command
			else if (args[1].contains("join")) {
				for (int i = 0; i < sesmax; i++) {
					if (e.getMember().getNickname() == players[i]) {
						e.getTextChannel().sendMessage(new MessageBuilder().append("You are already in this session.").build()).queue();
						return;
					}
				}
				for (int j = 0; j < sesmax; j++) {
					if (players[j] == "-") {
						players[j] = e.getMember().getNickname();
						sescount++;
						e.getTextChannel().sendMessage(new MessageBuilder().append(e.getMember().getNickname() + " has signed up for the session. Their spot is " + sescount + " out of " + sesmax).build()).queue();
						return;
					}
				}
				e.getTextChannel().sendMessage(new MessageBuilder().append("There are no more available slots left for this session").build()).queue();
				return;
			}
			//Leave Command
			else if (args[1].contains("leave")) {
				for (int i = 0; i < sesmax; i++) {
					if (players[i] == e.getMember().getNickname()) {
						players[i] = "-";
						e.getTextChannel().sendMessage(new MessageBuilder().append(e.getMember().getNickname() + " has left the session queue.").build()).queue();
						sescount--;
						return;
					} else {
						e.getTextChannel().sendMessage(new MessageBuilder().append("You are not in a session queue.").build()).queue();
						return;
					}
				}
			}
			//Delete Command
			else if (args[1].contains("delete")) {
				if (e.getAuthor().getId().contains(creator)) {
					session = false;
					creator = "-";
					sescount = 0;
					for (int i = 0; i < sesmax; i++) {
						players[i] = "-";
					}
					e.getTextChannel().sendMessage(new MessageBuilder().append("Session was successfully deleted by " + e.getMember().getNickname() + ".").build()).queue();
					return;
				} else {
					e.getTextChannel().sendMessage(new MessageBuilder().append("You are not the creator of the session.").build()).queue();
					return;
				}
			}
			//List Command
			else if (args[1].contains("list")) {
				if (session == false) {
					e.getTextChannel().sendMessage(new MessageBuilder().append("There is no session running.").build()).queue();
					return;
				}
				StringBuilder builder = new StringBuilder();
				EmbedBuilder embed = new EmbedBuilder();
				e.getTextChannel().sendMessage(new MessageBuilder().append("__**Player list for current session**__\n").build()).queue();
				for (int i = 0; i < sesmax; i++) {
					if (players[i] == "-") {
						builder.append("There is nobody in this spot. (" + (i + 1) + " of " + sesmax + ")\n");
					}
					else {
						builder.append("**" + players[i] + "** (" + (i + 1) + " of " + sesmax + ")\n");
					}
				}
				embed.setDescription(builder);
				e.getTextChannel().sendMessage(embed.build()).queue();
				return;
			}
			else {
				e.getTextChannel().sendMessage(new MessageBuilder().append("This is not a valid extension.\n Possibilities: Create, Join, Leave, Delete, List").build()).queue();
			}
		}
	}
}
