package com.ruoyi.graphprocesser.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.graphprocesser.mapper.OfficialModelMapper;
import com.ruoyi.graphprocesser.domain.OfficialModel;
import com.ruoyi.graphprocesser.service.IOfficialModelService;

/**
 * 官方模型Service业务层处理
 * 
 * @author zgs
 * @date 2024-11-10
 */
@Service
public class OfficialModelServiceImpl implements IOfficialModelService 
{
    @Autowired
    private OfficialModelMapper officialModelMapper;

    /**
     * 查询官方模型
     * 
     * @param id 官方模型主键
     * @return 官方模型
     */
    @Override
    public OfficialModel selectOfficialModelById(Long id)
    {
        return officialModelMapper.selectOfficialModelById(id);
    }

    /**
     * 查询官方模型列表
     * 
     * @param officialModel 官方模型
     * @return 官方模型
     */
    @Override
    public List<OfficialModel> selectOfficialModelList(OfficialModel officialModel)
    {
        return officialModelMapper.selectOfficialModelList(officialModel);
    }

    /**
     * 新增官方模型
     * 
     * @param officialModel 官方模型
     * @return 结果
     */
    @Override
    public int insertOfficialModel(OfficialModel officialModel)
    {
        officialModel.setCreateTime(DateUtils.getNowDate());
        return officialModelMapper.insertOfficialModel(officialModel);
    }

    /**
     * 修改官方模型
     * 
     * @param officialModel 官方模型
     * @return 结果
     */
    @Override
    public int updateOfficialModel(OfficialModel officialModel)
    {
        officialModel.setUpdateTime(DateUtils.getNowDate());
        return officialModelMapper.updateOfficialModel(officialModel);
    }

    /**
     * 批量删除官方模型
     * 
     * @param ids 需要删除的官方模型主键
     * @return 结果
     */
    @Override
    public int deleteOfficialModelByIds(Long[] ids)
    {
        return officialModelMapper.deleteOfficialModelByIds(ids);
    }

    /**
     * 删除官方模型信息
     * 
     * @param id 官方模型主键
     * @return 结果
     */
    @Override
    public int deleteOfficialModelById(Long id)
    {
        return officialModelMapper.deleteOfficialModelById(id);
    }
}
