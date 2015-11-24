package sooglejay.youtu.api.imagetag;

import java.util.List;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class ImageTagResponseBean {

//    字段	类型	说明
//    errorcode	Int32	返回状态码,非0值为出错
//    errormsg	String	返回错误描述
//    tags	ImageTag	图像的分类标签
//    seq	String	请求中的序列号
    private int errorcode;
    private String errormsg;
    private List<ImageTagItem> tags;
    private String seq;
}
