package kg.kloop.android.openbudgetapp;

import java.io.Serializable;

class Tender implements Serializable {
    private String text;

    public Tender() {
    }

    public Tender(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
