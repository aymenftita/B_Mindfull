package tn.esprit.mindfull.entity;

import java.time.LocalDateTime;
// a websocket payload here
public class NotificationMessage {
    private int id;
    private String message;
    private LocalDateTime timestamp;
    private String type;
    private int userId;
    private boolean read;

    public NotificationMessage() {}

    public NotificationMessage(int id,
                               String message,
                               LocalDateTime timestamp,
                               String type,
                               int userId,
                               boolean read) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.userId = userId;
        this.read = read;
    }

    // ────────────────────────────────────────────────────────────────────────
    // getters & setters
    // ────────────────────────────────────────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
}
