package com.ruoyi.graphprocesser.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.graphprocesser.mapper.DatasetMapper;
import com.ruoyi.graphprocesser.domain.Dataset;
import com.ruoyi.graphprocesser.service.IDatasetService;

/**
 * 数据集Service业务层处理
 * 
 * @author zgs
 * @date 2024-10-26
 */
@Service
public class DatasetServiceImpl implements IDatasetService 
{
    @Autowired
    private DatasetMapper datasetMapper;

    /**
     * 查询数据集
     * 
     * @param id 数据集主键
     * @return 数据集
     */
    @Override
    public Dataset selectDatasetById(Long id)
    {
        return datasetMapper.selectDatasetById(id);
    }

    /**
     * 查询数据集列表
     * 
     * @param dataset 数据集
     * @return 数据集
     */
    @Override
    public List<Dataset> selectDatasetList(Dataset dataset)
    {
        return datasetMapper.selectDatasetList(dataset);
    }

    /**
     * 新增数据集
     * 
     * @param dataset 数据集
     * @return 结果
     */
    @Override
    public int insertDataset(Dataset dataset)
    {
        dataset.setCreateTime(DateUtils.getNowDate());
        return datasetMapper.insertDataset(dataset);
    }

    /**
     * 修改数据集
     * 
     * @param dataset 数据集
     * @return 结果
     */
    @Override
    public int updateDataset(Dataset dataset)
    {
        dataset.setUpdateTime(DateUtils.getNowDate());
        return datasetMapper.updateDataset(dataset);
    }

    /**
     * 批量删除数据集
     * 
     * @param ids 需要删除的数据集主键
     * @return 结果
     */
    @Override
    public int deleteDatasetByIds(Long[] ids)
    {
        return datasetMapper.deleteDatasetByIds(ids);
    }

    /**
     * 删除数据集信息
     * 
     * @param id 数据集主键
     * @return 结果
     */
    @Override
    public int deleteDatasetById(Long id)
    {
        return datasetMapper.deleteDatasetById(id);
    }
}
