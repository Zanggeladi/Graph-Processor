package com.ruoyi.graphprocesser.mapper;

import java.util.List;
import com.ruoyi.graphprocesser.domain.Dataset;

/**
 * 数据集Mapper接口
 * 
 * @author zgs
 * @date 2024-10-26
 */
public interface DatasetMapper 
{
    /**
     * 查询数据集
     * 
     * @param id 数据集主键
     * @return 数据集
     */
    public Dataset selectDatasetById(Long id);

    /**
     * 查询数据集列表
     * 
     * @param dataset 数据集
     * @return 数据集集合
     */
    public List<Dataset> selectDatasetList(Dataset dataset);

    /**
     * 新增数据集
     * 
     * @param dataset 数据集
     * @return 结果
     */
    public int insertDataset(Dataset dataset);

    /**
     * 修改数据集
     * 
     * @param dataset 数据集
     * @return 结果
     */
    public int updateDataset(Dataset dataset);

    /**
     * 删除数据集
     * 
     * @param id 数据集主键
     * @return 结果
     */
    public int deleteDatasetById(Long id);

    /**
     * 批量删除数据集
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDatasetByIds(Long[] ids);
}
