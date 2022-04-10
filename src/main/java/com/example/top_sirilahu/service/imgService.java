package com.example.top_sirilahu.service;

import com.alibaba.fastjson.JSON;
import com.example.top_sirilahu.Exception.FileFormatNotCorrect;
import com.example.top_sirilahu.VO.imageVO;
import com.example.top_sirilahu.VO.recordVO;
import com.example.top_sirilahu.entity.imgEntity;
import com.example.top_sirilahu.jsonBody.paginationJSON;
import com.example.top_sirilahu.jsonBody.statusJSON;
import com.example.top_sirilahu.repository.imgRepository;
import com.example.top_sirilahu.util.fileUtil;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.SimpleRouteMatcher;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.NoResultException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class imgService {
    private static String rootPath;
    private static int pageSize;

    private imgRepository imgRepo;

    @Autowired
    public imgService(imgRepository imgRepo) {
        this.imgRepo = imgRepo;
    }

    @Value("${image.pagesize}")
    public void setPageSize(int pageSize) {
        imgService.pageSize = pageSize;
    }

    @Value("${file.upload.rootPath}")
    public void setRootPath(String rootPath) {
        imgService.rootPath = rootPath;
    }

    /**
     * 分页获取图片上传记录
     * @param page 页码
     * @param UID 请求用户ID
     * @return 获取到的上传记录转换成的JSON
     */
    public String getImgInfoPagination(int page, long UID) {
        List images = null;
        //进行分页计算
        int count = imgRepo.countByUploader(UID);
        int pageCount = 1;

        if (count > 0) {
            pageCount = (int) Math.ceil((double) count / (double) pageSize);
        }else{
            throw new NoResultException("[info]没有图片上传记录");
        }
        //检查查询参数是否异常
        if (page > pageCount) {
            throw new InvalidParameterException("[erro]查询参数异常");
        }

        //获取用户的分页记录本
        images = imgRepo.getImagesPagination(UID, (page - 1) * pageSize, pageSize);

        //业务对象转换
        images = imageVO.convertToAll(images);

        //返回json
        return JSON.toJSONString(new paginationJSON(0, pageCount, page, images));
    }

    /**
     * 图片上传服务
     *
     * @param img 待上传的图片
     * @param UID 用户ID
     * @return 图片的上传路径
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public String imgUpload(MultipartFile img, long UID) throws IOException {

        //生成上传文件存储名
        String originalFilename = img.getOriginalFilename();
        String imgName = String.format("%d.%s", Calendar.getInstance().getTimeInMillis(), originalFilename.substring(originalFilename.lastIndexOf(".") + 1));
        String path = String.format("%d/img/%s", UID, imgName);

        //拼接储存路径
        String fullPath = rootPath + path;

        //将图片信息存储至数据库
        imgRepo.saveImg(new imgEntity(imgName, path, UID));
        //保存图片
        fileUtil.saveMultipartFileFile(img, fullPath);

        return path;
    }

    @Transactional(rollbackFor = Exception.class)
    public void  delImg(long imgID) throws IOException {
        //获取待删除的图片对象
        Optional<imgEntity> optional = imgRepo.findById(imgID);
        if (optional.isPresent())
        {
            throw new NoResultException("删除图片不存在");
        }
        imgEntity img = optional.get();

        //操作数据库指定图片信息
        imgRepo.deleteById(imgID);

        //删除图片
        fileUtil.delFile(rootPath + img.getPath());
    }
}
