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
    public void upload() {
        //设置点播服务配置项构造方法
        VodConfig vodConfig = new VodConfig();

        //必选，设置项目ID，参考前提条件获取
        vodConfig.setProjectId("projectId");

        //必选，设置点播的Endpoint，参考前提条件获取
        vodConfig.setEndPoint("https://vod.cn-north-4.myhuaweicloud.com");

        //必选，设置临时AK、SK、securitytoken，参考前提条件获取
        vodConfig.setAk("ak");
        vodConfig.setSk("sk");
        vodConfig.setToken("securityToken");

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
        FileMeta fileMeta = new FileMeta("filePath", "bucket", "objectKey");

        /*
         * 传入FileMeta后开始上传，不支持回调
         * CompleteMultipartUploadResult result = vodClient.multipartUploadFile(fileMeta);
         */

        // 开始上传，可以传入一个回调内部类来实时获取上传信息。
        CompleteMultipartUploadResult result = vodClient.multipartUploadFile(fileMeta, new ObsService.OnPartEtagUploadedListener() {

            @Override  //初始化段结束时回调
            public void onInitMultiUploadPart(String uploadId) {
                Log.d("Init end: ", uploadId+"");
            }

            @Override   //上传完每一段时回调,可以获取回调时的PartEtag，上传进度。
            public void onUploadEachPart(PartEtag partEtag, String uploadId, int progress) {
                Log.d(partEtag.toString() + " is end !  Progress: ", progress + "");
            }

            @Override   // 上传完成时回调
            public void onCompleteMultiUpload(String uploadId) {
                Log.d("Complete end: ", uploadId + "");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
        //返回处理消息
        Log.d("result", new Gson().toJson(result));
    }
    public static void main(String[] args) {

    }
}
