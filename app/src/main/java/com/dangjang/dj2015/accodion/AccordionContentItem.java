package com.dangjang.dj2015.accodion;

import android.text.Spanned;

/**
 * Created by Tacademy on 2015-10-23.
 */
public class AccordionContentItem {
    String content;
    Spanned spanned;

    public AccordionContentItem(String content) {
        this.content = content;
    }
    public AccordionContentItem(Spanned spanned) {
        this.spanned = spanned;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Spanned getSpanned() {
        return spanned;
    }

    public void setSpanned(Spanned spanned) {
        this.spanned = spanned;
    }
}
