package com.wz.lanyue.banke.model;

import cc.solart.dragdrop.IDragEntity;

/**
 * Created by BANBEICHAS on 2016/6/6.
 */
public class SimpleDragEntity implements IDragEntity {
    private int id;
    private String name;

    public SimpleDragEntity() {
    }

    public SimpleDragEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
