package com.example.top_sirilahu.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class fileUtil {
    /**
     * 保存页文件
     *
     * @param pageContent
     * @param path
     */
    public static void savePage(String pageContent, String path) throws IOException {
        //前置准备
        File file = new File(path);
        if (!file.exists()) {
            File dir = new File(path.substring(0, path.lastIndexOf("/")));
            dir.mkdirs();
            file.createNewFile();
        }

        //按行拆分文件内容
        String[] rows = pageContent.split("\n");

        if (rows.length < 1) {
            throw new IOException("不允许保存空文件");
        }
        //逐行保存文件
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(file))//创建指向指定路径的字符流
        ) {
            for (String row : rows) {
                //按行写入
                writer.write(row);
                //换行
                writer.newLine();
            }
            //刷新写入
            writer.flush();
        }
    }

    /**
     * 删除路径指向的文件
     *
     * @param path 待删除的文件路径
     */
    public static void delFile(String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            throw new IOException("删除文件不存在");
        }

        file.delete();
    }

    /**
     * 页文件中是否含有关键字
     *
     * @param path  搜索文件路径
     * @param field 搜索关键字
     * @return 搜索文件是否含有该关键字
     */
    public static boolean isPageHaveField(String path, String field) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("页文件不存在");
        }


        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))//创建字节输入流
        ) {
            //读取首行
            String row = reader.readLine();
            while (row != null) {
                if (row.indexOf(field) != -1) {
                    return true;
                }

                //读取下一行
                row = reader.readLine();
            }
        }

        return false;
    }

    /**
     * 保存多媒体文件
     *
     * @param multipartFile 待保存的多媒体文件
     * @param path          保存路径
     * @throws IOException
     */
    public static void saveMultipartFileFile(MultipartFile multipartFile, String path) throws IOException {
        //拼接出上传地址
        File dest = new File(path);

        if (!dest.exists()) {
            File dir = new File(path.substring(0, path.lastIndexOf("/")));
            dir.mkdirs();
            dest.createNewFile();
        }

        //执行上传
        multipartFile.transferTo(dest);
    }

}
