package me.t3sl4.ondergrup.Model.OnBoard;

public class OnBoard {
    private int backgroundColor;
    private String text;
    private int imageRes;

    public OnBoard(int backgroundColor, String text, int imageRes) {
        this.backgroundColor = backgroundColor;
        this.text = text;
        this.imageRes = imageRes;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public String getText() {
        return text;
    }

    public int getImageRes() {
        return imageRes;
    }
}