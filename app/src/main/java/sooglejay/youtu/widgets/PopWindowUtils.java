package sooglejay.youtu.widgets;

import android.app.Activity;
import android.widget.PopupWindow;

/**
 * Created by sooglejay on 2015/10/25.
 */
public class PopWindowUtils {

    private PopupWindow pop;

    public PopWindowUtils(Activity mContext) {
        this.mContext = mContext;
    }

    private Activity mContext;

    public void dismiss() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }

    public boolean isShowing() {
        if (pop != null) {
            return pop.isShowing();
        }
        return false;
    }

    public interface OnPopWindowDismissListener {
        public void onDismissListener();
    }

    public OnPopWindowDismissListener onPopWindowDismissListener;

    public void setOnPopWindowDismissListener(OnPopWindowDismissListener onPopWindowDismissListener) {
        this.onPopWindowDismissListener = onPopWindowDismissListener;
    }
}
