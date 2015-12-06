package sooglejay.youtu.utils;

import android.app.Activity;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMEvernoteHandler;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.controller.media.EvernoteShareContent;
import com.umeng.socialize.facebook.controller.UMFacebookHandler;
import com.umeng.socialize.facebook.media.FaceBookShareContent;
import com.umeng.socialize.flickr.controller.UMFlickrHandler;
import com.umeng.socialize.flickr.media.FlickrShareContent;
import com.umeng.socialize.instagram.controller.UMInstagramHandler;
import com.umeng.socialize.instagram.media.InstagramShareContent;
import com.umeng.socialize.kakao.controller.UMKakaoHandler;
import com.umeng.socialize.kakao.media.KakaoShareContent;
import com.umeng.socialize.laiwang.controller.UMLWHandler;
import com.umeng.socialize.line.controller.UMLineHandler;
import com.umeng.socialize.line.media.LineShareContent;
import com.umeng.socialize.linkedin.controller.UMLinkedInHandler;
import com.umeng.socialize.linkedin.media.LinkedInShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.pinterest.controller.UMPinterestHandler;
import com.umeng.socialize.pinterest.media.PinterestShareContent;
import com.umeng.socialize.pocket.controller.UMPocketHandler;
import com.umeng.socialize.pocket.media.PocketShareContent;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.tumblr.controller.UMTumblrHandler;
import com.umeng.socialize.tumblr.media.TumblrShareContent;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.whatsapp.controller.UMWhatsAppHandler;
import com.umeng.socialize.whatsapp.media.WhatsAppShareContent;
import com.umeng.socialize.yixin.controller.UMYXHandler;
import com.umeng.socialize.ynote.controller.UMYNoteHandler;
import com.umeng.socialize.ynote.media.YNoteShareContent;

import sooglejay.youtu.R;

/**
 * Created by JammyQtheLab on 2015/11/6.
 */
public class ShareUtils{
    
    
    
    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");
    private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
    
    private String imageFilePath;

    public ShareUtils(Activity activity, String imageFilePath) {
        this.activity = activity;
        this.imageFilePath = imageFilePath;
    }

    private Activity activity;

 


    /**
     * 添加所有的平台</br>
     */
    public void addCustomPlatforms(final Activity context) {
        // 添加微信平台
        addWXPlatform(context);
        // 添加QQ平台
        addQQQZonePlatform(context);
        // 添加印象笔记平台
        addEverNote(context);
        // 添加facebook平台
        addFacebook(context);
        // 添加Instagram平台
        addInstagram(context);
        // 添加来往、来往动态平台
//        addLaiWang(context);
        // 添加LinkedIn平台
//        addLinkedIn(context);
        // 添加Pinterest平台
        addPinterest(context);
        // 添加Pocket平台
        addPocket(context);
        // 添加有道云平台
        addYNote(context);
        // 添加易信平台
        addYXPlatform(context);
        // 添加短信平台
        addSMS();
        // 添加email平台
        addEmail();

//        addWhatsApp(context);
//        addLine(context);
//        addTumblr(context);
//        addkakao(context);
//        addFlickr(context);

        mController.registerListener(new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {
//                Toast.makeText(context, "share start...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//                Toast.makeText(context, "code : " + eCode, Toast.LENGTH_SHORT).show();
            }
        });

        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT,
                SHARE_MEDIA.DOUBAN,
                SHARE_MEDIA.RENREN, SHARE_MEDIA.EMAIL, SHARE_MEDIA.EVERNOTE, SHARE_MEDIA.FACEBOOK,
                SHARE_MEDIA.GOOGLEPLUS, SHARE_MEDIA.INSTAGRAM, SHARE_MEDIA.LAIWANG,
                SHARE_MEDIA.LAIWANG_DYNAMIC, SHARE_MEDIA.LINKEDIN, SHARE_MEDIA.PINTEREST,
                SHARE_MEDIA.POCKET, SHARE_MEDIA.SMS, SHARE_MEDIA.TWITTER, SHARE_MEDIA.YIXIN,
                SHARE_MEDIA.YIXIN_CIRCLE, SHARE_MEDIA.YNOTE, SHARE_MEDIA.WHATSAPP,
                SHARE_MEDIA.LINE, SHARE_MEDIA.TUMBLR, SHARE_MEDIA.FLICKR, SHARE_MEDIA.KAKAO);
        mController.openShare(context, false);
        mController.setShareImage(new UMImage(context,imageFilePath));
    }



    /**
     * 添加短信平台</br>
     */
    private void addSMS() {
        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
    }

    /**
     * 添加Email平台</br>
     */
    private void addEmail() {
        // 添加email
        EmailHandler emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();
    }

    /**
     * Pocket分享。pockect只支持分享网络链接</br>
     */
    private void addPocket(Activity context) {
        UMPocketHandler pocketHandler = new UMPocketHandler(context);
        pocketHandler.addToSocialSDK();
        PocketShareContent pocketShareContent = new PocketShareContent();
        pocketShareContent.setShareContent("责任和信任就是男人的霸气和浪漫");
        pocketShareContent.setShareImage(new UMImage(context,imageFilePath));
        mController.setShareMedia(pocketShareContent);
    }

    /**
     * LinkedIn分享。LinkedIn只支持图片，文本，图文分享</br>
     */
    private void addLinkedIn(Activity context) {
        UMLinkedInHandler linkedInHandler = new UMLinkedInHandler(context);
        linkedInHandler.addToSocialSDK();
        LinkedInShareContent linkedInShareContent = new LinkedInShareContent();
        linkedInShareContent
                .setShareContent("责任和信任就是男人的霸气和浪漫");
        mController.setShareMedia(linkedInShareContent);
    }

    /**
     * 有道云笔记分享。有道云笔记只支持图片，文本，图文分享</br>
     */
    private void addYNote(Activity context) {
        UMYNoteHandler yNoteHandler = new UMYNoteHandler(context);
        yNoteHandler.addToSocialSDK();
        YNoteShareContent yNoteShareContent = new YNoteShareContent();
        yNoteShareContent
                .setShareContent("责任和信任就是男人的霸气和浪漫 ");
        yNoteShareContent.setTitle("责任和信任就是男人的霸气和浪漫");
        yNoteShareContent.setShareImage(new UMImage(context, imageFilePath));
        mController.setShareMedia(yNoteShareContent);
    }

    /**
     * 添加印象笔记平台
     */
    private void addEverNote(Activity context) {
        UMEvernoteHandler evernoteHandler = new UMEvernoteHandler(context);
        evernoteHandler.addToSocialSDK();

        // 设置evernote的分享内容
        EvernoteShareContent shareContent = new EvernoteShareContent(
                "责任和信任就是男人的霸气和浪漫");
        shareContent.setShareMedia(new UMImage(context, imageFilePath));
        mController.setShareMedia(shareContent);
    }

    /**
     * 添加Pinterest平台
     */
    private void addPinterest(Activity context) {
        /**
         * app id需到pinterest开发网站( https://developers.pinterest.com/ )自行申请.
         */
        UMPinterestHandler pinterestHandler = new UMPinterestHandler(
                context, "1439206");
        pinterestHandler.addToSocialSDK();

        // 设置Pinterest的分享内容
        PinterestShareContent shareContent = new PinterestShareContent(
                "责任和信任就是男人的霸气和浪漫");
        shareContent.setShareMedia(new UMImage(context, imageFilePath));
        mController.setShareMedia(shareContent);
    }

    /**
     * 添加来往和来往动态平台</br>
     */
    private void addLaiWang(Activity context) {

        String appToken = "laiwangd497e70d4";
        String secretID = "d497e70d4c3e4efeab1381476bac4c5e";
        // laiwangd497e70d4:来往appToken,d497e70d4c3e4efeab1381476bac4c5e:来往secretID
        // 添加来往的支持
        UMLWHandler umlwHandler = new UMLWHandler(context, appToken, secretID);
        umlwHandler.addToSocialSDK();

        // 添加来往动态的支持
        UMLWHandler lwDynamicHandler = new UMLWHandler(context,
                appToken, secretID);
        lwDynamicHandler.setToCircle(true);
        lwDynamicHandler.addToSocialSDK();
    }

    /**
     * @功能描述 : 添加微信平台分享
     * @return
     */
    private void addWXPlatform(Activity context) {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx967daebe835fbeac";
        String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     * @return
     */
    private void addQQQZonePlatform(Activity context) {
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context,
                appId, appKey);
        
        qqSsoHandler.addToSocialSDK();
        qqSsoHandler.setTitle(context.getString(R.string.app_name));

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * @Title: addYXPlatform
     * @Description:
     * @throws
     */
    private void addYXPlatform(Activity context) {

        // 添加易信平台
        UMYXHandler yixinHandler = new UMYXHandler(context,
                "yxc0614e80c9304c11b0391514d09f13bf");
        // 关闭分享时的等待Dialog
        yixinHandler.enableLoadingDialog(false);
        // 设置target Url, 必须以http或者https开头
        yixinHandler.setTitle("责任和信任就是男人的霸气和浪漫");
        yixinHandler.addToSocialSDK();

        // 易信朋友圈平台
        UMYXHandler yxCircleHandler = new UMYXHandler(context,
                "yxc0614e80c9304c11b0391514d09f13bf");
        yxCircleHandler.setToCircle(true);
        yxCircleHandler.addToSocialSDK();

    }

    /**
     * @Title: addFacebook
     * @Description:
     * @throws
     */
    private void addFacebook(Activity context) {

        UMFacebookHandler mFacebookHandler = new UMFacebookHandler(context);
        mFacebookHandler.addToSocialSDK();

        UMImage localImage = new UMImage(context,imageFilePath);

        UMVideo umVedio = new UMVideo(
                "http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
        umVedio.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
        umVedio.setTitle("责任和信任就是男人的霸气和浪漫");

        FaceBookShareContent fbContent = new FaceBookShareContent();
//         fbContent.setShareImage(new UMImage(context,
//         "http://www.umeng.com/images/pic/social/integrated_3.png"));
//        fbContent.setShareImage(localImage);
//         fbContent.setShareContent("This is my facebook social share sdk.");
//        fbContent.setShareVideo(umVedio);
        fbContent.setCaption("Caption - Fb");
        fbContent.setShareImage(localImage);
        fbContent.setShareContent("Wish 赛亚人 success in the games!");
        fbContent.setTitle(context.getString(R.string.app_name));
        mController.setShareMedia(fbContent);

    }

    /**
     * </br> Instagram只支持图片分享, 只支持纯图片分享.</br>
     */
    private void addInstagram(Activity context) {
        // 构建Instagram的Handler
        UMInstagramHandler instagramHandler = new UMInstagramHandler(
                context);
        instagramHandler.addToSocialSDK();

        UMImage localImage = new UMImage(context, imageFilePath);

        // // 添加分享到Instagram的内容
        InstagramShareContent instagramShareContent = new InstagramShareContent(
                localImage);
        mController.setShareMedia(instagramShareContent);
    }

    private void addWhatsApp(Activity context) {
        UMWhatsAppHandler whatsAppHandler = new UMWhatsAppHandler(context);
        whatsAppHandler.addToSocialSDK();
        WhatsAppShareContent whatsAppShareContent = new WhatsAppShareContent();
        // whatsAppShareContent.setShareContent("share ic_menu");
        whatsAppShareContent.setShareImage(new UMImage(context, imageFilePath));
        mController.setShareMedia(whatsAppShareContent);
        // mController.openShare(context, false);
    }

    private void addLine(Activity context) {
        UMLineHandler lineHandler = new UMLineHandler(context);
        lineHandler.addToSocialSDK();
        LineShareContent lineShareContent = new LineShareContent();
        // lineShareContent.setShareContent("share ic_menu");
        lineShareContent.setShareImage(new UMImage(context,imageFilePath));
        mController.setShareMedia(lineShareContent);
        // mController.openShare(context, false);
    }

    private void addTumblr(Activity context) {
        UMTumblrHandler tumblrHandler = new UMTumblrHandler(context);
        tumblrHandler.addToSocialSDK();
        TumblrShareContent tumblrShareContent = new TumblrShareContent();
        tumblrShareContent.setTitle(context.getString(R.string.app_name));
        tumblrShareContent.setShareContent("责任和信任就是男人的霸气和浪漫");
        tumblrShareContent.setShareImage(new UMImage(context, imageFilePath));
        mController.setShareMedia(tumblrShareContent);
        // mController.openShare(context, false);
    }

    private void addkakao(Activity context) {
        UMKakaoHandler kakaoHandler = new UMKakaoHandler(context);
        kakaoHandler.addToSocialSDK();

        KakaoShareContent kakaoShareContent = new KakaoShareContent();
        // kakaoShareContent.setShareContent("share ic_menu");
        kakaoShareContent.setShareImage(new UMImage(context, imageFilePath));
        mController.setShareMedia(kakaoShareContent);
        // mController.openShare(context, false);
    }

    private void addFlickr(Activity context) {
        UMFlickrHandler flickrHandler = new UMFlickrHandler(context);
        flickrHandler.addToSocialSDK();
        FlickrShareContent flickrShareContent = new FlickrShareContent();
        flickrShareContent.setShareImage(new UMImage(context, imageFilePath));
        // flickrShareContent.setShareContent("share ic_menu");
        mController.setShareMedia(flickrShareContent);
        // mController.openShare(context, false);
    }

}
