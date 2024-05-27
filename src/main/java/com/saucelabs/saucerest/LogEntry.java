package com.saucelabs.saucerest;

public class LogEntry {
  private final String timestamp;
  private final String level;
  private final String message;

  public LogEntry(String time, String level, String message) {
    this.timestamp = time;
    this.level = level;
    this.message = message;
  }

  public String getTime() {
    return timestamp;
  }

  public String getLevel() {
    return level;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return String.format("%s %s %s", timestamp, level, message);
  }
}
