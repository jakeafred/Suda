package dead.bot.suda;

import dead.bot.suda.player.SudaPlayer;
import dead.bot.suda.util.Uptime;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import dead.bot.suda.commands.*;
import java.io.IOException;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.entities.Game;

public class Suda extends ListenerAdapter {
	public static Uptime uptime;
	public static void main(String[] args)
			throws IOException, IllegalArgumentException, LoginException, RateLimitedException, InterruptedException {

		Settings settings = SettingsManager.getInstance().getSettings();
		JDABuilder jdaBuild = new JDABuilder(AccountType.BOT).setToken(settings.getBotToken());

		HelpCommand help = new HelpCommand();
		jdaBuild.addEventListener(help.registerCommand(help));
		jdaBuild.addEventListener(help.registerCommand(new PlayCommand()));
		jdaBuild.addEventListener(help.registerCommand(new SkipCommand()));
		jdaBuild.addEventListener(help.registerCommand(new PauseCommand()));
		jdaBuild.addEventListener(help.registerCommand(new CurrentSongCommand()));
		jdaBuild.addEventListener(help.registerCommand(new InfoCommand()));
		jdaBuild.addEventListener(help.registerCommand(new SessionCommand()));
		jdaBuild.addEventListener(help.registerCommand(new DiceCommand()));
		jdaBuild.addEventListener(help.registerCommand(new ListCommand()));
		jdaBuild.addEventListener(help.registerCommand(new StopCommand()));
		jdaBuild.addEventListener(help.registerCommand(new VolumeCommand()));
		jdaBuild.addEventListener(new ShutdownCommand());
		jdaBuild.addEventListener(new SudaPlayer());
		jdaBuild.setGame(Game.playing("Type .help"));
		jdaBuild.buildBlocking();
		while (true) {
			uptime = new Uptime();
		}
	}
}