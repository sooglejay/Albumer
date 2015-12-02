package sooglejay.youtu.test;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import sooglejay.youtu.R;
import sooglejay.youtu.bean.GroupBean;
import sooglejay.youtu.db.GroupNameDao;
import sooglejay.youtu.ui.BaseActivity;

/**
 * Created by JammyQtheLab on 2015/12/2.
 */
public class ORMTestActivity extends BaseActivity {
    private EditText et_name;
    private TextView tv_add, tv_delete, tv_result, tv_update;
    private GroupNameDao groupNameDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aaa_test);
        groupNameDao = new GroupNameDao(this);

        et_name = (EditText) findViewById(R.id.et_name);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tv_update = (TextView) findViewById(R.id.tv_update);
        tv_result = (TextView) findViewById(R.id.tv_result);

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String etStr = et_name.getText().toString();
                if (!TextUtils.isEmpty(etStr)) {
                    GroupBean bean = new GroupBean();
                    bean.setIsSelected(false);
                    bean.setName(etStr);
                    groupNameDao.add(bean);
                }
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String etStr = et_name.getText().toString();
                if (!TextUtils.isEmpty(etStr)) {
                    groupNameDao.deleteByName(etStr);
                }
            }
        });

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_result.setText("");
                List<GroupBean> groupBeans = groupNameDao.getAll();
                for (GroupBean bean : groupBeans) {
                    tv_result.append(bean.getName());
                }
            }
        });


    }
}
