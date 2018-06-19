package dead.bot.suda;

public class Settings {
	
	public String token;
	public String ownerId;
	
	public String getBotToken() {
		return token;
	}
	
	public void setBotToken(String token) {
		this.token = token;
	}
	
	public String getOwner() {
		return ownerId;
	}
	
	public void setOwner(String ownerId) {
		this.ownerId = ownerId;
	}
}
