package sooglejay.youtu.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by JammyQtheLab on 2015/12/1.
 */

@DatabaseTable(tableName = "tb_group_name")
public class GroupBean {
    @DatabaseField
    private String name;

    @DatabaseField
    private boolean isSelected;//这个字段是用于 adapter的显示的


     @DatabaseField
    private boolean isUsedForIdentify;//这个字段是用于是否做为人脸识别的groupid



    @DatabaseField(generatedId = true)
    private int id;
    @Override
    public String toString() {
        return "GroupBean{" +
                "name='" + name + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public GroupBean() {
    }

}
