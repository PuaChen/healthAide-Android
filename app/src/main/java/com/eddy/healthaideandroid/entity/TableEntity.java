package com.eddy.healthaideandroid.entity;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

/**
 * Created with Android Studio
 * Author:Chen·ZD
 * Date:2019/5/15
 */

@SmartTable(name = "指标")
public class TableEntity {
    @SmartColumn(id = 2, name = "指标类型")
    private String name;

    @SmartColumn(id = 3, name = "指标值")
    private Double value;

    public TableEntity(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
