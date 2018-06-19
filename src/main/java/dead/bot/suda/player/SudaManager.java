package dead.bot.suda.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

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
}