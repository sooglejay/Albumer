package sooglejay.youtu.api;

import retrofit.http.Body;
import retrofit.http.POST;
import sooglejay.youtu.api.addface.AddFaceRequestBean;
import sooglejay.youtu.api.addface.AddFaceResponseBean;
import sooglejay.youtu.api.delface.DelFaceRequestBean;
import sooglejay.youtu.api.delface.DelFaceResponseBean;
import sooglejay.youtu.api.delperson.DelPersonRequestBean;
import sooglejay.youtu.api.delperson.DelPersonResponseBean;
import sooglejay.youtu.api.detectface.DetectFaceRequestBean;
import sooglejay.youtu.api.detectface.DetectFaceResponseBean;
import sooglejay.youtu.api.facecompare.FaceCompareRequestBean;
import sooglejay.youtu.api.facecompare.FaceCompareResponseBean;
import sooglejay.youtu.api.faceidentify.FaceIdentifyRequestBean;
import sooglejay.youtu.api.faceidentify.FaceIdentifyResponseBean;
import sooglejay.youtu.api.faceshape.FaceShapeRequestBean;
import sooglejay.youtu.api.faceshape.FaceShapeResponseBean;
import sooglejay.youtu.api.faceverify.FaceVerifyRequestBean;
import sooglejay.youtu.api.faceverify.FaceVerifyResponseBean;
import sooglejay.youtu.api.fooddetct.FoodDetectRequestBean;
import sooglejay.youtu.api.fooddetct.FoodDetectResponseBean;
import sooglejay.youtu.api.fuzzydetect.FuzzyDetectRequestBean;
import sooglejay.youtu.api.fuzzydetect.FuzzyDetectResponseBean;
import sooglejay.youtu.api.getfaceids.GetFaceIdsRequestBean;
import sooglejay.youtu.api.getfaceids.GetFaceIdsResponseBean;
import sooglejay.youtu.api.getfaceinfo.GetFaceInfoRequestBean;
import sooglejay.youtu.api.getfaceinfo.GetFaceInfoResponseBean;
import sooglejay.youtu.api.getgroupids.GetGroupIdsRequestBean;
import sooglejay.youtu.api.getgroupids.GetGroupIdsResponseBean;
import sooglejay.youtu.api.getinfo.GetInfoReponseBean;
import sooglejay.youtu.api.getinfo.GetInfoRequestBean;
import sooglejay.youtu.api.getpersonids.GetPersonIdsRequestBean;
import sooglejay.youtu.api.getpersonids.GetPersonIdsResponseBean;
import sooglejay.youtu.api.imagetag.ImageTagRequestBean;
import sooglejay.youtu.api.imagetag.ImageTagResponseBean;
import sooglejay.youtu.api.newperson.NewPersonRequestBean;
import sooglejay.youtu.api.newperson.NewPersonResponseBean;
import sooglejay.youtu.api.setinfo.SetInfoRequestBean;
import sooglejay.youtu.api.setinfo.SetInfoResponseBean;
import sooglejay.youtu.model.NetCallback;

/**
 * Tencent's You Tu Api List
 */
public interface YouTuApi {

    @POST("/api/facecompare")
    public void facecompare(@Body FaceCompareRequestBean bean, NetCallback<FaceCompareResponseBean> NetCallback);


    @POST("/api/detectface")
    public void detectface(@Body DetectFaceRequestBean bean, NetCallback<DetectFaceResponseBean> NetCallback);


    @POST("/api/faceshape")
    public void faceshape(@Body FaceShapeRequestBean bean, NetCallback<FaceShapeResponseBean> NetCallback);



    @POST("/api/faceverify")
    public void faceverify(@Body FaceVerifyRequestBean bean, NetCallback<FaceVerifyResponseBean> NetCallback);


    @POST("/api/faceidentify")
    public void faceidentify(@Body FaceIdentifyRequestBean bean, NetCallback<FaceIdentifyResponseBean> NetCallback);


    @POST("/api/newperson")
    public void newperson(@Body NewPersonRequestBean bean, NetCallback<NewPersonResponseBean> NetCallback);


    @POST("/api/delperson")
    public void delperson(@Body DelPersonRequestBean bean, NetCallback<DelPersonResponseBean> NetCallback);


    @POST("/api/addface")
    public void addface(@Body AddFaceRequestBean bean, NetCallback<AddFaceResponseBean> NetCallback);



    @POST("/api/delface")
    public void delface(@Body DelFaceRequestBean bean, NetCallback<DelFaceResponseBean> NetCallback);


    @POST("/api/setinfo")
    public void setinfo(@Body SetInfoRequestBean bean, NetCallback<SetInfoResponseBean> NetCallback);


    @POST("/api/getinfo")
    public void getinfo(@Body GetInfoRequestBean bean, NetCallback<GetInfoReponseBean> NetCallback);

    @POST("/api/getgroupids")
    public void getgroupids(@Body GetGroupIdsRequestBean bean, NetCallback<GetGroupIdsResponseBean> NetCallback);

    @POST("/api/getpersonids")
    public void getpersonids(@Body GetPersonIdsRequestBean bean, NetCallback<GetPersonIdsResponseBean> NetCallback);




    @POST("/api/getfaceids")
    public void getfaceids(@Body GetFaceIdsRequestBean bean, NetCallback<GetFaceIdsResponseBean> NetCallback);

    @POST("/api/getfaceinfo")
    public void getfaceinfo(@Body GetFaceInfoRequestBean bean, NetCallback<GetFaceInfoResponseBean> NetCallback);


    @POST("/api/fuzzydetect")
    public void fuzzydetect(@Body FuzzyDetectRequestBean bean, NetCallback<FuzzyDetectResponseBean> NetCallback);


    @POST("/api/fooddetct")
    public void fooddetct(@Body FoodDetectRequestBean bean, NetCallback<FoodDetectResponseBean> NetCallback);


    @POST("/api/imagetag")
    public void imagetag(@Body ImageTagRequestBean bean, NetCallback<ImageTagResponseBean> NetCallback);




}
