package com.example.top_sirilahu.service;

import com.example.top_sirilahu.entity.recordEntity;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.repository.recordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@ConfigurationProperties(prefix = "record")
public class recordService
{
    private static int pageSize = 8;
    recordRepository recordRepo;

    public recordService() { }
    @Autowired
    public recordService(recordRepository recordRepo) {
        this.recordRepo = recordRepo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    //获取用户的记录本数
    public int getCount(long UID)
    {
        return recordRepo.countByRCreator(UID);
    }

    //获取用户的记录本(分页显示)
    public void getRecords(userEntity user, int page, Model model)
    {
        //进行分页计算
        int count = getCount(user.getUID());
        int pageCount = 1;
        if (count != 0)
        {
            pageCount = (int) Math.ceil(  (double) count/ (double) pageSize );
        }

        //获取用户的分页记录本
        List<recordEntity> records =  recordRepo.getRecordsPagination(user.getUID(), ( page - 1 ) * pageSize, pageSize);

        model.addAttribute("records", records);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("page", page);
    }

    //添加记录本
    public void addRecord(recordEntity record, userEntity user)
    {
        //生成记录本编号
        int count = getCount(user.getUID());
        String r_id = String.format("%d-1", user.getUID());
        if (count > 0)
        {
            recordEntity lastRecord = recordRepo.getRecordsPagination(user.getUID(), 0, 1).get(0);
            String[] code = lastRecord.getR_id().split("-");
            r_id = String.format("%s-%d", code[0], Integer.parseInt(code[1]) + 1);
        }

        record.setR_id(r_id);

        record.setR_creator(user.getUID());

        //保存修改
        recordRepo.saveRecord(record);
    }

    //修改记录本
    public void editRecord(String r_name, String r_id)
    {
        recordRepo.update(r_name, r_id);
    }

    //删除记录本
    public void delRecord(String r_id)
    {
        recordRepo.deleteById(r_id);
    }
}
