package com.ruoyi.graphprocesser.mapper;

import java.util.List;
import com.ruoyi.graphprocesser.domain.ExperimentTheme;

/**
 * 对比实验主题Mapper接口
 * 
 * @author zgs
 * @date 2024-10-26
 */
public interface ExperimentThemeMapper 
{
    /**
     * 查询对比实验主题
     * 
     * @param id 对比实验主题主键
     * @return 对比实验主题
     */
    public ExperimentTheme selectExperimentThemeById(Long id);

    /**
     * 查询对比实验主题列表
     * 
     * @param experimentTheme 对比实验主题
     * @return 对比实验主题集合
     */
    public List<ExperimentTheme> selectExperimentThemeList(ExperimentTheme experimentTheme);

    /**
     * 新增对比实验主题
     * 
     * @param experimentTheme 对比实验主题
     * @return 结果
     */
    public int insertExperimentTheme(ExperimentTheme experimentTheme);

    /**
     * 修改对比实验主题
     * 
     * @param experimentTheme 对比实验主题
     * @return 结果
     */
    public int updateExperimentTheme(ExperimentTheme experimentTheme);

    /**
     * 删除对比实验主题
     * 
     * @param id 对比实验主题主键
     * @return 结果
     */
    public int deleteExperimentThemeById(Long id);

    /**
     * 批量删除对比实验主题
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExperimentThemeByIds(Long[] ids);
}
