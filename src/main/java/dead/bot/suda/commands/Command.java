package dead.bot.suda.commands;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

public abstract class Command extends ListenerAdapter
{
    public abstract void onCommand(MessageReceivedEvent e, String[] args);
    public abstract List<String> getCommands();
    public abstract String getDescription();
    public abstract String getName();
    public abstract List<String> getInstructions();

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
    	
        if (e.getAuthor().isBot()) {
        	return;
        }
        
        if (containsCommand(e.getMessage())) {
            onCommand(e, commandArgs(e.getMessage()));
        }
    }

    protected boolean containsCommand(Message message) {
        return getCommands().contains(commandArgs(message)[0]);
    }

    protected String[] commandArgs(Message message) {
        return commandArgs(message.getContentDisplay());
    }

    protected String[] commandArgs(String string) {
        return string.split(" ");
    }

    protected Message sendMessage(MessageReceivedEvent e, Message message) {
        if(e.isFromType(ChannelType.PRIVATE)) {
            return e.getPrivateChannel().sendMessage(message).complete();
        } else { 
            return e.getTextChannel().sendMessage(message).complete();
        }
    }

    protected Message sendMessage(MessageReceivedEvent e, String message) {
        return sendMessage(e, new MessageBuilder().append(message).build());
    }
}