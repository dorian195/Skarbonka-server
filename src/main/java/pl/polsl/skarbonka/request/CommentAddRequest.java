package pl.polsl.skarbonka.request;

public class CommentAddRequest {
    private Long fundraisingId;
    private String text;

    public CommentAddRequest(Long fundraisingId, String text) {
        this.fundraisingId = fundraisingId;
        this.text = text;
    }

    public CommentAddRequest() {
    }

    public Long getFundraisingId() {
        return fundraisingId;
    }

    public void setFundraisingId(Long fundraisingId) {
        this.fundraisingId = fundraisingId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
