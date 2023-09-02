package inc.seven.smsfilter;

public class SmsModel {
    String senderName, senderMessage;

    public SmsModel() {
    }

    public SmsModel(String senderName, String senderMessage) {
        this.senderName = senderName;
        this.senderMessage = senderMessage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }
}
