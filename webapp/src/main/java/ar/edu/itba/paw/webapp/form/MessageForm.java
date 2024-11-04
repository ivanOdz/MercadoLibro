

package ar.edu.itba.paw.webapp.form;


public class MessageForm {
    private Long userId;
    private Long chatExchangeId;
    private String message;

    public MessageForm() {}

    public MessageForm(Long userId, Long chatExchangeId, String message) {
        this.userId = userId;
        this.chatExchangeId = chatExchangeId;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChatExchangeId() {
        return chatExchangeId;
    }

    public void setChatExchangeId(Long chatExchangeId) {
        this.chatExchangeId = chatExchangeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
