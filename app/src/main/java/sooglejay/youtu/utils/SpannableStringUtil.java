package sooglejay.youtu.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import sooglejay.youtu.R;

/**
 * Created by JammyQtheLab on 2015/12/2.
 */
public class SpannableStringUtil {
    public SpannableString addUrlSpan(String str) {
        SpannableString spanString = new SpannableString(str);
        URLSpan span = new URLSpan("tel:0123456789");
        spanString.setSpan(span, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 文字背景颜色
     */
    public SpannableString addBackColorSpan(String str) {
        SpannableString spanString = new SpannableString(str);
        BackgroundColorSpan span = new BackgroundColorSpan(Color.YELLOW);
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;

    }


    /**
     * 文字颜色
     */
    public SpannableString addForeColorSpan(String str) {
        SpannableString spanString = new SpannableString(str);
        ForegroundColorSpan span = new ForegroundColorSpan(Color.BLUE);
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 字体大小
     */
    public SpannableString addFontSpan(String str) {
        SpannableString spanString = new SpannableString(str);
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(36);
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 粗体，斜体
     */
    public SpannableString addStyleSpan(String str) {
        SpannableString spanString = new SpannableString(str);
        StyleSpan span = new StyleSpan(Typeface.BOLD_ITALIC);
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    /**
     * 删除线
     */
    public SpannableString addStrikeSpan(String str) {
        SpannableString spanString = new SpannableString(str);
        StrikethroughSpan span = new StrikethroughSpan();
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 下划线
     */
    public static  SpannableString addUnderLineSpan(String str) {
        SpannableString spanString = new SpannableString(str);
        UnderlineSpan span = new UnderlineSpan();
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


}
