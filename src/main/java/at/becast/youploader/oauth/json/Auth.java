package at.becast.youploader.oauth.json;

public class Auth {
  public final String error;
  public final String error_description;
  public final String error_uri;
  public final String access_token;
  public final String token_type;
  public final int expires_in;
  public final String refresh_token;
  public final String scope;

  public Auth(String error, String error_description, String access_token, String token_type, int expires_in, String refresh_token, String error_uri, String scope) {
    this.error = error;
    this.error_description = error_description;
    this.access_token = access_token;
    this.token_type = token_type;
    this.expires_in = expires_in;
    this.refresh_token = refresh_token;
    this.error_uri = error_uri;
	this.scope = scope;
  }

  public Auth() {
    this(null, null, null, null, 0, null, null, null);
  }
}
