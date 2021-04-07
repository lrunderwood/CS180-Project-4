import java.io.Serializable;

final class ChatMessage implements Serializable {
    private static final long serialVersionUID = 6898543889087L;

    // Types of messages
    static final int MESSAGE = 0, LOGOUT = 1, DM = 2, LIST = 3, TICTACTOE = 4;

    // Variables, Constructors, Methods, etc.
    private int msgType;
    private String msg;
    private String recipient;

    public ChatMessage(int msgType, String msg, String recipient){
        this.msgType = msgType;
        this.msg = msg;
        this.recipient = recipient;
    }

    public int getMsgType(){
        return this.msgType;
    }
    public String getMsg(){
        return this.msg;
    }
    public String getRecipient(){
        return this.recipient;
    }
}
