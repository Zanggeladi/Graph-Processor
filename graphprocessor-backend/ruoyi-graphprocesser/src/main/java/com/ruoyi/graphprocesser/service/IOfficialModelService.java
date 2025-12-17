package com.ruoyi.graphprocesser.service;

import java.util.List;
import com.ruoyi.graphprocesser.domain.OfficialModel;

/**
 * 官方模型Service接口
 * 
 * @author zgs
 * @date 2024-11-10
 */
public interface IOfficialModelService 
{
    /**
     * 查询官方模型
     * 
     * @param id 官方模型主键
     * @return 官方模型
     */
    public OfficialModel selectOfficialModelById(Long id);

    /**
     * 查询官方模型列表
     * 
     * @param officialModel 官方模型
     * @return 官方模型集合
     */
    public List<OfficialModel> selectOfficialModelList(OfficialModel officialModel);

    /**
     * 新增官方模型
     * 
     * @param officialModel 官方模型
     * @return 结果
     */
    public int insertOfficialModel(OfficialModel officialModel);

    /**
     * 修改官方模型
     * 
     * @param officialModel 官方模型
     * @return 结果
     */
    public int updateOfficialModel(OfficialModel officialModel);

    /**
     * 批量删除官方模型
     * 
     * @param ids 需要删除的官方模型主键集合
     * @return 结果
     */
    public int deleteOfficialModelByIds(Long[] ids);

    /**
     * 删除官方模型信息
     * 
     * @param id 官方模型主键
     * @return 结果
     */
    public int deleteOfficialModelById(Long id);
}
