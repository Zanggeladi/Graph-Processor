package com.ruoyi.graphprocesser.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.graphprocesser.mapper.ExperimentThemeMapper;
import com.ruoyi.graphprocesser.domain.ExperimentTheme;
import com.ruoyi.graphprocesser.service.IExperimentThemeService;

/**
 * 对比实验主题Service业务层处理
 * 
 * @author zgs
 * @date 2024-10-26
 */
@Service
public class ExperimentThemeServiceImpl implements IExperimentThemeService 
{
    @Autowired
    private ExperimentThemeMapper experimentThemeMapper;

    /**
     * 查询对比实验主题
     * 
     * @param id 对比实验主题主键
     * @return 对比实验主题
     */
    @Override
    public ExperimentTheme selectExperimentThemeById(Long id)
    {
        return experimentThemeMapper.selectExperimentThemeById(id);
    }

    /**
     * 查询对比实验主题列表
     * 
     * @param experimentTheme 对比实验主题
     * @return 对比实验主题
     */
    @Override
    public List<ExperimentTheme> selectExperimentThemeList(ExperimentTheme experimentTheme)
    {
        return experimentThemeMapper.selectExperimentThemeList(experimentTheme);
    }

    /**
     * 新增对比实验主题
     * 
     * @param experimentTheme 对比实验主题
     * @return 结果
     */
    @Override
    public int insertExperimentTheme(ExperimentTheme experimentTheme)
    {
        experimentTheme.setCreateTime(DateUtils.getNowDate());
        return experimentThemeMapper.insertExperimentTheme(experimentTheme);
    }

    /**
     * 修改对比实验主题
     * 
     * @param experimentTheme 对比实验主题
     * @return 结果
     */
    @Override
    public int updateExperimentTheme(ExperimentTheme experimentTheme)
    {
        experimentTheme.setUpdateTime(DateUtils.getNowDate());
        return experimentThemeMapper.updateExperimentTheme(experimentTheme);
    }

    /**
     * 批量删除对比实验主题
     * 
     * @param ids 需要删除的对比实验主题主键
     * @return 结果
     */
    @Override
    public int deleteExperimentThemeByIds(Long[] ids)
    {
        return experimentThemeMapper.deleteExperimentThemeByIds(ids);
    }

    /**
     * 删除对比实验主题信息
     * 
     * @param id 对比实验主题主键
     * @return 结果
     */
    @Override
    public int deleteExperimentThemeById(Long id)
    {
        return experimentThemeMapper.deleteExperimentThemeById(id);
    }
}
