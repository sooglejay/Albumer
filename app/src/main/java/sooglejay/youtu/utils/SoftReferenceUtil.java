package sooglejay.youtu.utils;

import android.os.Parcelable;

import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * Created by JammyQtheLab on 2015/11/28.
 */
public class SoftReferenceUtil<T> extends SoftReference implements Serializable {

    public SoftReferenceUtil(Object r) {
        super(r);
    }

    public SoftReferenceUtil(Object r, ReferenceQueue q) {
        super(r, q);
    }
}
