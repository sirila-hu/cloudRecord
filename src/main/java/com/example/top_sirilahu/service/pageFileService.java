package com.example.top_sirilahu.service;

import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.repository.pageRepository;
import com.example.top_sirilahu.util.fileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;

@Service
public class pageFileService {
    private static String rootPath;
    private pageRepository pageRepo;

    @Value("${file.upload.rootPath}")
    public void setRootPath(String rootPath) {
        pageFileService.rootPath = rootPath;
    }

    @Autowired
    public pageFileService(pageRepository pageRepo) {
        this.pageRepo = pageRepo;
    }

    /**
     * 保存页文件服务
     *
     * @param pageContent 待保存的页文件内容
     * @param p_id        页ID 用于生成页文件的文件名和更新页信息
     * @return            页文件的保存路径
     * @throws Exception 可能出现IO错误或数据库相关错误
     */
    @Transactional(rollbackFor = Exception.class)
    public String savePage(String pageContent, String p_id, Long UID) throws Exception {
        //拼接页文件存储路径( 用户ID/页文件存储目录/文件名 )
        String p_path = String.format("%d/pageFiles/%s.md", UID, p_id);
        String fullPath = rootPath + p_path;
        //生成预览文本
        int index = 20;
        String p_pre = pageContent.replace("\n", "");
        if (p_pre.length() < 20) {
            index = p_pre.length();
        }
        p_pre = p_pre.substring(0, index);

        //更新数据库信息
        pageRepo.updatePage(p_pre, p_path, p_id);

        //调用文件工具保存页文件
        fileUtil.savePage(pageContent, fullPath);

        //返回文件保存路径
        return p_path;
    }

    /**
     * 删除页服务
     * 考虑到细粒度删除的问题，该逻辑使用独立的事务
     * @param page 待删除的页对象
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void delPage(pageEntity page) throws IOException {
        //拼接删除路径
        String fullPath = rootPath + page.getP_path();
        //操作数据库
        pageRepo.deleteById(page.getP_id());

        if (!StringUtils.isEmptyOrWhitespace(page.getP_path()))
        {
            //删除页文件
            fileUtil.delFile(fullPath);
        }
    }
}
