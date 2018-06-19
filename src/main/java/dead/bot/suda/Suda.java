package dead.bot.suda;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import dead.bot.suda.player.SudaManager;
import dead.bot.suda.player.SudaPlayer;
import dead.bot.suda.util.Uptime;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import dead.bot.suda.commands.*;
import java.io.IOException;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import java.util.HashMap;
import java.util.Map;

public class Suda extends ListenerAdapter {
	public static Uptime uptime;
	public static void main(String[] args)
			throws IOException, IllegalArgumentException, LoginException, RateLimitedException, InterruptedException {

		Settings settings = SettingsManager.getInstance().getSettings();
		JDABuilder jdaBuild = new JDABuilder(AccountType.BOT).setToken(settings.getBotToken());

		HelpCommand help = new HelpCommand();
		jdaBuild.addEventListener(help.registerCommand(help));
		//jdaBuild.addEventListener(help.registerCommand(new PlayCommand()));
		//jdaBuild.addEventListener(help.registerCommand(new SkipCommand()));
		//jdaBuild.addEventListener(help.registerCommand(new PauseCommand()));
		//jdaBuild.addEventListener(help.registerCommand(new CurrentSongCommand()));
		jdaBuild.addEventListener(help.registerCommand(new InfoCommand()));
		jdaBuild.addEventListener(help.registerCommand(new ShutdownCommand()));
		jdaBuild.addEventListener(help.registerCommand(new SessionCommand()));
		jdaBuild.addEventListener(help.registerCommand(new DiceCommand()));
		//jdaBuild.addEventListener(new SudaPlayer());
		jdaBuild.setGame(Game.playing("Type .help"));
		jdaBuild.buildBlocking();
		while (true) {
			uptime = new Uptime();
		}
	}
}
/*if (!event.getMessage().getContentRaw().startsWith("!play")) return;
		if (event.getAuthor().isBot()) return;
		Guild guild = event.getGuild();
		VoiceChannel channel = getUserVoiceChannel(event.getMember());
		AudioManager manager = guild.getAudioManager();
		AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);

		AudioPlayer player = playerManager.createPlayer();
		manager.setSendingHandler(new AudioPlayerSendHandler(player));
		manager.openAudioConnection(channel);
	}
	public VoiceChannel getUserVoiceChannel(Member member) {
		return member.getVoiceState().getChannel();
	}*/

