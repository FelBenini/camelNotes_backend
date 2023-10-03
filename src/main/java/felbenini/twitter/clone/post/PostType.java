package felbenini.twitter.clone.post;

public enum PostType {
  POST("POST"),
  REPLY("REPLY");

  private String type;

  PostType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }
}
