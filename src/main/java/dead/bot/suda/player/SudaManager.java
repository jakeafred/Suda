package dead.bot.suda.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.core.entities.Guild;

public class SudaManager {
	
  public final AudioPlayer player;
  public final TrackScheduler scheduler;
  public final AudioPlayerSendHandler sendHandler;

  public SudaManager(AudioPlayerManager manager) {
    player = manager.createPlayer();
    scheduler = new TrackScheduler(player);
    sendHandler = new AudioPlayerSendHandler(player);
    player.addListener(scheduler);
  }
  
public static SudaManager getMusicManager(Guild guild) {
		String guildId = guild.getId();
		SudaManager manager = SudaPlayer.musicManagers.get(guildId);
		if (manager == null) {
			synchronized (SudaPlayer.musicManagers) {
				manager = SudaPlayer.musicManagers.get(guildId);
				if (manager == null) {
					manager = new SudaManager(SudaPlayer.playerManager);
					manager.player.setVolume(30);
					SudaPlayer.musicManagers.put(guildId, manager);
				}
			}
		}
		return manager;
	}

public static String getTimestamp(long milliseconds){
    int seconds = (int) (milliseconds / 1000) % 60 ;
    int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
    int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

    if (hours > 0)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    else
        return String.format("%02d:%02d", minutes, seconds);
}
}