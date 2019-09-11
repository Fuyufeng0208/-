package com.changgou.file.test;


import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;


/**
 * Created by fyf on 2019/8/19
 */
public class FastdfsClientTest {
    /**
     * 文件上传
     * @throws Exception
     */
    @Test
    public void upload() throws Exception {
        //加载全局的配置文件
        ClientGlobal.init("E:\\idea crack\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过trackerClient获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取StorageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //执行上传文件
        String[] jpgs = storageClient.upload_file("E:\\img\\Dilraba02.jpg", "jpg", null);
        for (String jpg : jpgs) {
            System.out.println(jpg);
        }
    }

    /**
     * 删除文件
     * @throws Exception
     */
    @Test
    public void delete()throws Exception{
        //加载全局的配置文件
        ClientGlobal.init("E:\\idea crack\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过trackerClient获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取StorageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //执行删除文件
        int i = storageClient.delete_file("group1", "M00/00/00/wKjThF1aiWSAYOj0AAFtWzPZdBM603.jpg");
        if (i==0) {
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");
        }
    }

    /**
     * 文件下载
     * @throws Exception
     */
    @Test
    public void download()throws Exception{
        //加载全局的配置文件
        ClientGlobal.init("E:\\idea crack\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过trackerClient获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取StorageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, null);

        //执行下载操作
        byte[] bytes = storageClient.download_file("group1", "M00/00/00/wKjThF1akJSAP9n8AAFtWzPZdBM915.jpg");
        File file = new File("D:\\img\\Dilraba02.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(bytes);
        bufferedOutputStream.close();
        fileOutputStream.close();
    }

    /**
     * 获取文件信息
     * @throws Exception
     */
    @Test
    public void getFileInfo()throws Exception{
        //加载全局的配置文件
        ClientGlobal.init("E:\\idea crack\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过trackerClient获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取StorageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, null);

        FileInfo group1 = storageClient.get_file_info("group1", "M00/00/00/wKjThF1akJSAP9n8AAFtWzPZdBM915.jpg");
        System.out.println(group1);
    }

    /**
     * 获取组相关的信息
     * @throws Exception
     */
    @Test
    public void getGroupInfo()throws Exception{
        //加载全局的配置文件
        ClientGlobal.init("E:\\idea crack\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //创建TrackerClient客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过trackerClient获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageServer group1 = trackerClient.getStoreStorage(trackerServer, "group1");
        System.out.println(group1.getStorePathIndex());

        //组对应的服务器的地址  因为有可能有多个服务器.
        ServerInfo[] group1s = trackerClient.getFetchStorages(trackerServer, "group1", "M00/00/00/wKjThF1akJSAP9n8AAFtWzPZdBM915.jpg");
        for (ServerInfo serverInfo : group1s) {
            System.out.println(serverInfo.getIpAddr());
            System.out.println(serverInfo.getPort());
        }
    }

    @Test
    public void getTrackerInfo() throws Exception {
        //加载全局的配置文件
        ClientGlobal.init("E:\\idea crack\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");

        //创建TrackerClient客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient对象获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();

        InetSocketAddress inetSocketAddress = trackerServer.getInetSocketAddress();
        System.out.println(inetSocketAddress);

    }
}
