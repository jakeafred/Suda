package dead.bot.suda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SettingsManager {

	private static SettingsManager instance;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	public Settings settings;
	private final Path config = new File(".").toPath().resolve("Config.json");

	public static SettingsManager getInstance() {
		if (instance == null) {
			instance = new SettingsManager();
		}
		return instance;
	}

	public Settings getSettings() {
		return settings;
	}

	public SettingsManager() {
		if (!config.toFile().exists()) {
			System.out.println("Suda: Creating configuration file...");
			System.out.println("Suda: Please edit the Config.json file I just created.");
			this.settings = getDefault();
			save();
			System.exit(0);
		}
		load();
	}

	public void load() {
		try {
			BufferedReader reader = Files.newBufferedReader(config, StandardCharsets.UTF_8);
			this.settings = gson.fromJson(reader, Settings.class);
			reader.close();
			System.out.println("Suda: Completed Loading Settings...");
			save();
		} catch (IOException e) {
			System.out.println("Suda: Error When Loading Settings.");
			e.printStackTrace();
		}
	}
	public void save() {
		String jsonOut = gson.toJson(this.settings);
		try {
			BufferedWriter writer = Files.newBufferedWriter(config, StandardCharsets.UTF_8);
			writer.append(jsonOut);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private Settings getDefault() {
		Settings newSettings = new Settings();
		newSettings.setBotToken("");
		newSettings.setOwner("");

		return newSettings;
	}
}
