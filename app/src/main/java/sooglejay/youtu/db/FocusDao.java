package sooglejay.youtu.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sooglejay.youtu.bean.FocusBean;


public class FocusDao {
    private Dao<FocusBean, Integer> focusDaoOpe;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public FocusDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            focusDaoOpe = helper.getDao(FocusBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个FocusBean
     *
     * @param FocusBean
     */
    public void add(FocusBean FocusBean) {
        try {
            focusDaoOpe.create(FocusBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过Id得到一个FocusBean记录
     * @param id
     * @return
     */
    public FocusBean get(int id) {
        FocusBean bean = null;
        try {
            bean = focusDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    /**
     * 获取表中数据记录个数
     *
     * @return
     */
    public int getCount() {
        int count =  0;
        try {
            count = focusDaoOpe.queryForAll().size();
        } catch (SQLException e) {
            e.printStackTrace();
            count = 0 ;
        }finally {
            return count;
        }
    }
    /**
     * 获取所有的FocusBean
     *
     * @return
     */
    public List<FocusBean> getAll() {
        try {
            return focusDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void deleteByName(String imagePathName) {
        try {
            DeleteBuilder<FocusBean, Integer> deleteBuilder = focusDaoOpe.deleteBuilder();
            deleteBuilder.where().eq("imagePath", imagePathName);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            focusDaoOpe.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void uodateGroupNameBean(FocusBean bean) {
        try {
            focusDaoOpe.update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
