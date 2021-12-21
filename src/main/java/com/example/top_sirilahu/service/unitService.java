package com.example.top_sirilahu.service;

import com.example.top_sirilahu.Exception.FileFormatNotCorrect;
import com.example.top_sirilahu.entity.pageEntity;
import com.example.top_sirilahu.entity.unitEntity;
import com.example.top_sirilahu.repository.unitRepository;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.NoResultException;
import javax.persistence.Transient;
import javax.validation.ValidationException;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.NoSuchElementException;

@ConfigurationProperties(prefix = "unit.imgupload")
@Service
public class unitService
{
    private unitRepository unitRepo;
    private String imgUploadPath;

    public unitService() {
    }

    @Autowired
    public unitService(unitRepository unitRepo) {
        this.unitRepo = unitRepo;
    }

    public void setImgUploadPath(String imgUploadPath) {
        this.imgUploadPath = imgUploadPath;
    }

    //获取单元
    public void getUnits(pageEntity page)
    {
        page.setUnits(unitRepo.getUnits(page.getP_id()));
    }

    //文件上传
    public String imgUpload(MultipartFile img) throws IOException, FileFormatNotCorrect {
        if (img.getContentType().indexOf("image") == -1)
        {
            throw new FileFormatNotCorrect("文件格式不正确，只接受图像类型数据");
        }
        //生成上传文件存储名
        String originalFilename =img.getOriginalFilename();
        String fileName = String.format("%d.%s", Calendar.getInstance().getTimeInMillis(), originalFilename.substring(originalFilename.lastIndexOf(".")+1));
        //拼接出上传地址
        File dest = new File(imgUploadPath + fileName);
        //执行上传
        img.transferTo(dest);

        return fileName;
    }

    //添加单元
    public String addUnit(unitEntity unit)
    {
        //生成分区编号
        int count = unitRepo.countByU_page(unit.getU_page());
        String u_id = String.format("%s-1",unit.getU_page());
        if (count != 0)
        {
            unitEntity lastUnit = unitRepo.getLastUnit(unit.getU_page());
            String[] code = lastUnit.getU_id().split("-");
            u_id = String.format("%s-%d", unit.getU_page(),Integer.parseInt(code[code.length - 1]) + 1);
        }

        unit.setU_id(u_id);

        //保存section
        unitRepo.saveUnit(unit);

        return u_id;
    }

    //修改单元文本内容
    public void editUnit(unitEntity unit)
    {
        if (StringUtils.isEmptyOrWhitespace(unit.getU_id()))
        {
            throw new ValidationException("主键为空");
        }

        int count = unitRepo.editUnit(unit);

        if (count == 0)
        {
            throw new NoResultException("修改失败");
        }
    }

    //修改单元文本内容
    @Transient
    public String editUnit(MultipartFile img, String u_id) throws FileFormatNotCorrect, IOException, NoResultException
    {
        //新图片上传
        String newImg = imgUpload(img);

        //获取旧图片的文件名
        String oldImg = unitRepo.findById(u_id).get().getU_img();
        try {
            //修改单元路径
            int count = unitRepo.editUnit(newImg, u_id);
            if (count == 0) {
                throw new NoResultException("图片更改失败");
            }

            new File(imgUploadPath + oldImg).delete();
        }catch (Exception e)
        {
            e.printStackTrace();
            new File(imgUploadPath + newImg).delete();
        }
        return newImg;
    }

    //删除单元
    @Transient
    public void delUnit(String u_id)throws NoSuchElementException
    {
        //获取选择的单元的图片位置
        String u_img = unitRepo.findById(u_id).get().getU_img();

        //删除单元数据
        unitRepo.deleteById(u_id);

        //删除图片
        new File(imgUploadPath + u_img).delete();
    }

}
