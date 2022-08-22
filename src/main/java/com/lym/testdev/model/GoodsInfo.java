package com.lym.testdev.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class GoodsInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String id;
    /**
     *
     */
    private String title;
    /**
     *
     */
    private String classify;
    /**
     *
     */
    private BigDecimal price;
    /**
     *
     */
    private String details;

    /**
     *
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     */
    public String getId() {
        return id;
    }

    /**
     *
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     */
    public void setClassify(String classify) {
        this.classify = classify;
    }

    /**
     *
     */
    public String getClassify() {
        return classify;
    }

    /**
     *
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     *
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     *
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     *
     */
    public String getDetails() {
        return details;
    }
}
