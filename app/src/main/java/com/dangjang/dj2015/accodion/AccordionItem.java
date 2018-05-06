package com.dangjang.dj2015.accodion;

/**
 * Created by Tacademy on 2015-10-29.
 */
public class AccordionItem {
    private AccordionTitleItem title;
    private AccordionContentItem content;

    public AccordionTitleItem getTitle() {
        return title;
    }

    public void setTitle(AccordionTitleItem title) {
        this.title = title;
    }

    public AccordionContentItem getContent() {
        return content;
    }

    public void setContents(AccordionContentItem content) {
        this.content = content;
    }
}
