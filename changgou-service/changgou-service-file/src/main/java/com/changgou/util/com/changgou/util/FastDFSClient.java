package com.changgou.util.com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by fyf on 2019/8/19
 */
public class FastDFSClient {
    /**
     * 初始化tracker信息
     */
    static {
        try {
            //获取tracker的配置文件fdfs_client.conf的位置
            String filePath = new ClassPathResource("fdfs_client.conf").getPath();
            //加载tracker配置信息
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片上传
     *
     * @param file 要上传的文件
     * @return
     */
    public static String[] upload(FastDFSFile file) {
        try {
            //创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            //通过TrackerClient对象获取TrackerServer信息
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取StorageClient对象
            StorageClient storageClient = new StorageClient(trackerServer, null);
            //执行文件上传
            NameValuePair[] metal_list = new NameValuePair[]{new NameValuePair(file.getName()), new NameValuePair(file.getAuthor())};
            String[] strings = storageClient.upload_file(file.getContent(), file.getExt(), metal_list);

            return strings;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片下载
     *
     * @param groupName      组名
     * @param remoteFileName 文件存储完整名
     * @return
     */
    public static InputStream downFile(String groupName, String remoteFileName) {
        try {
            //创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            //通过trackerClient获取TrackerServer信息
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取StorageClient对象
            StorageClient storageClient = new StorageClient(trackerServer, null);
            byte[] bytes = storageClient.download_file(groupName, remoteFileName);
            return new ByteArrayInputStream(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除图片
     *
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName, String remoteFileName) {
        try {
            //创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            //通过trackerClient获取TrackerServer信息
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取StorageClient对象
            StorageClient storageClient = new StorageClient(trackerServer, null);

            //执行删除文件
            int i = storageClient.delete_file(groupName, remoteFileName);
            if (i == 0) {
                System.out.println("删除成功");
            } else {
                System.out.println("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据文件名和组名获取文件信息
     * @param groupName 组名
     * @param remoteFileName 文件存储完整名
     * @return
     */
    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            //创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            //通过trackerClient获取TrackerServer信息
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取StorageClient对象
            StorageClient storageClient = new StorageClient(trackerServer, null);

            FileInfo fileInfo = storageClient.get_file_info(groupName, remoteFileName);
            return fileInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据组名获取文件信息
     * @param groupName 组名
     * @return
     */
    public static StorageServer getStorages(String groupName) {
        try {
            //创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            //通过trackerClient获取TrackerServer信息
            TrackerServer trackerServer = trackerClient.getConnection();
            StorageServer group1 = trackerClient.getStoreStorage(trackerServer, groupName);
            return group1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据文件名和组名获取组信息的数组信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName){
        try {
            //创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            //通过trackerClient获取TrackerServer信息
            TrackerServer trackerServer = trackerClient.getConnection();
            ServerInfo[] serverInfos = trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
            return serverInfos;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Tracker的IP和端口信息
     * @return
     */
    public static String getTrackerUrl(){
        try {
            //创建TrackerClient客户端对象
            TrackerClient trackerClient = new TrackerClient();
            //通过trackerClient获取TrackerServer信息
            TrackerServer trackerServer = trackerClient.getConnection();
            String hostString = trackerServer.getInetSocketAddress().getHostString();
            int port = ClientGlobal.getG_tracker_http_port();
            return "http://"+hostString+":"+port;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
