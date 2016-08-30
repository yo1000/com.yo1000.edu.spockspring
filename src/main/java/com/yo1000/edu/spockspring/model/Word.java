package com.yo1000.edu.spockspring.model;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * @author yo1000
 */
public class Word {
    @NotEmpty
    private String text;
    private Date created;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
