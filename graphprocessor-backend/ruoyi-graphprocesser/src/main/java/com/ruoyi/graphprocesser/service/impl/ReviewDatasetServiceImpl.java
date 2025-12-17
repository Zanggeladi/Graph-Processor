package com.ruoyi.graphprocesser.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.graphprocesser.mapper.ReviewDatasetMapper;
import com.ruoyi.graphprocesser.domain.ReviewDataset;
import com.ruoyi.graphprocesser.service.IReviewDatasetService;

/**
 * 审核数据集Service业务层处理
 * 
 * @author zgs
 * @date 2024-09-10
 */
@Service
public class ReviewDatasetServiceImpl implements IReviewDatasetService
{
    @Autowired
    private ReviewDatasetMapper reviewDatasetMapper;

    /**
     * 查询审核数据集
     * 
     * @param datasetId 审核数据集主键
     * @return 审核数据集
     */
    @Override
    public ReviewDataset selectReviewDatasetByDatasetId(Long datasetId)
    {
        return reviewDatasetMapper.selectReviewDatasetByDatasetId(datasetId);
    }

    /**
     * 查询审核数据集列表
     * 
     * @param reviewDataset 审核数据集
     * @return 审核数据集
     */
    @Override
    public List<ReviewDataset> selectReviewDatasetList(ReviewDataset reviewDataset)
    {
        return reviewDatasetMapper.selectReviewDatasetList(reviewDataset);
    }

    /**
     * 新增审核数据集
     * 
     * @param reviewDataset 审核数据集
     * @return 结果
     */
    @Override
    public int insertReviewDataset(ReviewDataset reviewDataset)
    {
        reviewDataset.setCreateTime(DateUtils.getNowDate());
        return reviewDatasetMapper.insertReviewDataset(reviewDataset);
    }

    /**
     * 修改审核数据集
     * 
     * @param reviewDataset 审核数据集
     * @return 结果
     */
    @Override
    public int updateReviewDataset(ReviewDataset reviewDataset)
    {
        reviewDataset.setUpdateTime(DateUtils.getNowDate());
        return reviewDatasetMapper.updateReviewDataset(reviewDataset);
    }

    /**
     * 批量删除审核数据集
     * 
     * @param datasetIds 需要删除的审核数据集主键
     * @return 结果
     */
    @Override
    public int deleteReviewDatasetByDatasetIds(Long[] datasetIds)
    {
        return reviewDatasetMapper.deleteReviewDatasetByDatasetIds(datasetIds);
    }

    /**
     * 删除审核数据集信息
     * 
     * @param datasetId 审核数据集主键
     * @return 结果
     */
    @Override
    public int deleteReviewDatasetByDatasetId(Long datasetId)
    {
        return reviewDatasetMapper.deleteReviewDatasetByDatasetId(datasetId);
    }
}
