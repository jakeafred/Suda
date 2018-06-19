package dead.bot.suda.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class DiceCommand extends Command{
	private int numRoll = 0;
	private int dice = 0;
	private int modifier = 0;
	private String[] express;
	private String[] express2;
	private char op;
	private Random roll = new Random();
	private String equation = "";

	@Override
	public List<String> getCommands() {
		return Arrays.asList(".roll");
	}

	@Override
	public String getDescription() {
		return "Rolls a selected dice with added modifiers";
	}

	@Override
	public String getName() {
		return "Dice Command";
	}

	@Override
	public List<String> getInstructions() {
		return Arrays.asList("\n`.roll <dice expression>` - Rolls the selected dice with added modifiers.\n Ex: .roll 1d20+4");
	}

	public int roll(int dice, int op, int mod) {
		int num = 0;
		num = roll.nextInt(dice - 1) + 1;
		if (op == '+') {
			equation = Integer.toString(num) + " + " + Integer.toString(mod) + " = ";
			num += mod;
		} else if (op == '-') {
			equation = Integer.toString(num) + " - " + Integer.toString(mod) + " = ";
			num -= mod;
		}
		return num;
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] args) {
		if (args.length < 2) {
			e.getTextChannel().sendMessage(new MessageBuilder().append("Command not used correctly. Please type use the following format:\n")
					.append(".roll <num of rolls>d<type of dice><modifier>").build()).queue();
			return;
		} else {
			if (args[1].contains("d")) {
				express = args[1].split("d");
				numRoll = Integer.parseInt(express[0]);
				if (express[1].contains("+")) {
					op = '+';
					express2 = express[1].split("\\+");
					modifier = Integer.parseInt(express2[1]);
					dice = Integer.parseInt(express2[0]);
				} else if (express[1].contains("-")){
					op = '-';
					express2 = express[1].split("\\-");
					modifier = Integer.parseInt(express2[1]);
					dice = Integer.parseInt(express2[0]);
				} else {
					modifier = 0;
					dice = Integer.parseInt(express[1]);
				}
				for (int i = 0; i < numRoll; i++) {
					int rolled = roll(dice, op, modifier);
					if (modifier != 0) {
						e.getTextChannel().sendMessage(new MessageBuilder().append(e.getAuthor().getAsMention() + " rolled a **" + equation)
								.append(rolled + "**.").build()).queue();
					} else {
						e.getTextChannel().sendMessage(new MessageBuilder().append(e.getAuthor().getAsMention() + " rolled a **")
								.append(rolled + "**.").build()).queue();
					}
				}
			}
			else {
				e.getTextChannel().sendMessage(new MessageBuilder().append("Command not used correctly. Please type use the following format:\n")
						.append(".roll <num of rolls>d<type of dice><modifier>").build()).queue();
				return;
			}
		}
	}

}
