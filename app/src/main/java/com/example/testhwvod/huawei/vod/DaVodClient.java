package com.example.testhwvod.huawei.vod;

import android.util.Log;

import com.google.gson.Gson;
import com.huawei.upload.common.obs.model.FileMeta;
import com.huawei.upload.vod.client.ObsConfig;
import com.huawei.upload.vod.client.VodClient;
import com.huawei.upload.vod.client.VodConfig;
import com.huawei.upload.vod.service.ObsService;
import com.obs.services.model.CompleteMultipartUploadResult;
import com.obs.services.model.PartEtag;



public class DaVodClient {
    public static void upload() {
        //设置点播服务配置项构造方法
        VodConfig vodConfig = new VodConfig();

        //必选，设置项目ID，参考前提条件获取
        vodConfig.setProjectId("0ab06cb3d100f2932ffcc00131d8b3dc");

        //必选，设置点播的Endpoint，参考前提条件获取
        vodConfig.setEndPoint("https://vod.cn-north-4.myhuaweicloud.com");

        //必选，设置临时AK、SK、securitytoken，参考前提条件获取
        vodConfig.setAk("E5ZA8U2CWI0CMUCIGSLK");
        vodConfig.setSk("WAhVh0BLUYTR22diZBumjzji0gUszcoFWy61u1QO");
        vodConfig.setToken("gQpjbi1ub3J0aC00i15LoMtLgBof9deEzA47oB1ttSnz0IEJMH63WsMkh7BWe3o--3umO3trHkT1g3S2vXMSXnuu4V2o_GwC0sR85aSD1sgFXE6gNtn6LHrA7LNjKuPGVwPN9_TYqx5I4rwR4KS6Xs6ZPWkPqfY7UA_eRuxLW4cwX1O06_qaa0fl0KDnqp1EGgTZby3OR07erYSPlHfHyPruRMnvfFwHlEHSFJImzBE6qYLIKPBZ6UkbgKDzNwv7-SK9G6r21F-P3IUrL6ZibveWhTcMslGgfd4Ma9VfOaYW4AXaZhZ_DwgmbFWnNbamZD0eNO8AErIaAvCmRl3ntRtH-8hsjuLI0ckq46FdTjtxXvAaHzWzvWYRsZmbMgrWS-uDx71tOLfnris8DqvmBPYm7b8DFbtdX1TGY7207QLMUZUsh0bK2iO61g5pTjxluKtf_C11G3R9fOYX5HpOFq8xjafgerFJOwiDJT_GhIwn3T3edms1aGJyaoEtBsgASKH33lkEWWsO-pLd0OeT-uEowfmDkRRP2O8InMnqSYfpEilWs-qhw6RcMa4uDW6mNRa9r_iq73PrRQZ8AZb0RJulwGsbrdBO-x_WKP-YwZIa1UgIsqRPUZCbbJCOswjOaS9qTeHJiHbz-mEombs_Wf8rEkoqNIzUcx1LzvaJredx05smmKUu1kh1-S03k94mLGet3Rv7RaB-dM_dQOHhT3u8uUSJC261hAZVNBiqsGTBsmbZ8ryghSa6I_4wTXQ98_PNp6quN02MgIvVX4aAO_n1t-JUjStMN43q_niFzILlbejw4YcHpmTDMRkg");

        /*if you need proxy*/
        //可选，设置代理，根据实际情况选择设置
        /*
        ClientConfig clientConfig = new ClientConfig();
        //设置代理服务器主机IP
        clientConfig.setProxyHost(proxyHost);
        //设置代理服务器端口号
        clientConfig.setProxyPort(Integer.parseInt(proxyPort));
        //设置代理服务器用户名
        clientConfig.setProxyUserName(proxyUserName);
        //设置代理服务器用户密码
        clientConfig.setProxyPassword(proxyPassword);
        */
        //设置连接OBS的相关配置
        ObsConfig obsConfig = new ObsConfig();
        //必选，OBS的Endpoint，设置的OBS和点播服务需要在同一个区域
        obsConfig.setEndPoint("obs.cn-north-4.myhuaweicloud.com");
        //设置上传文件时每个分段的大小，单位为M，最小5M
        obsConfig.setPartSize(5);
        //设置是否分段并发上传，传入参数为并发数量，当入参小于等于1时代表串行上传。
        obsConfig.setConcurrencyLevel(0);



        //点播服务构造方法，在没有配置代理的情况下使用该构造方法
        VodClient vodClient=new VodClient(vodConfig, obsConfig);

        /*
         * proxy provided
         * //点播服务构造方法，在需要配置代理的情况下使用该构造方法
         * VodClient vodClient=new VodClient(VodConfig, clientConfig, obsConfig);
         */

        /**初始化上传信息,需要传入待上传文件的本地文件地址（绝对路径）、媒资所在的桶名、媒资所在的ObjectKey
         *    此处的filePath指本地文件的文件地址的绝对路径，如 d://test.mp4
         *    bucket、objectKey 需要服务端调用创建媒资接口后获得并返回给客户端，详情需要参考点播服务端的创建媒资接口
         */
        FileMeta fileMeta = new FileMeta("/storage/emulated/0/Download/test.mp4", "vod-bucket-27-cn-north-4", "0ab06cb3d100f2932ffcc00131d8b3dc/2b64370c1c3093ef2b15482b0a1e97eb/7d2530faf41a318503c48922b44030b3.mp4");

        /*
         * 传入FileMeta后开始上传，不支持回调
         * CompleteMultipartUploadResult result = vodClient.multipartUploadFile(fileMeta);
         */

        // 开始上传，可以传入一个回调内部类来实时获取上传信息。
        CompleteMultipartUploadResult result = vodClient.multipartUploadFile(fileMeta, new ObsService.OnPartEtagUploadedListener() {

            @Override  //初始化段结束时回调
            public void onInitMultiUploadPart(String uploadId) {
                Log.d("Init end: ", uploadId+"");
//                System.out.println("inited!:" + uploadId);
            }

            @Override   //上传完每一段时回调,可以获取回调时的PartEtag，上传进度。
            public void onUploadEachPart(PartEtag partEtag, String uploadId, int progress) {
                Log.d(partEtag.toString() + " is end !  Progress: ", progress + "");
//                System.out.printf("progress of part %s: %d\n", partEtag.toString(), progress);
            }

            @Override   // 上传完成时回调
            public void onCompleteMultiUpload(String uploadId) {
                Log.d("Complete end: ", uploadId + "");
//                System.out.println("success:" + uploadId);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
        //返回处理消息
        Log.d("result", new Gson().toJson(result));
    }

}
