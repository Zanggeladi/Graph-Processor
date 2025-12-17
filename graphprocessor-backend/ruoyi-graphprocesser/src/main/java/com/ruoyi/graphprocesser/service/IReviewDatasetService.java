package com.ruoyi.graphprocesser.service;

import java.util.List;

import com.ruoyi.graphprocesser.domain.ReviewDataset;

/**
 * 审核数据集Service接口
 * 
 * @author zgs
 * @date 2024-09-10
 */
public interface IReviewDatasetService
{
    /**
     * 查询审核数据集
     * 
     * @param datasetId 审核数据集主键
     * @return 审核数据集
     */
    public ReviewDataset selectReviewDatasetByDatasetId(Long datasetId);

    /**
     * 查询审核数据集列表
     * 
     * @param reviewDataset 审核数据集
     * @return 审核数据集集合
     */
    public List<ReviewDataset> selectReviewDatasetList(ReviewDataset reviewDataset);

    /**
     * 新增审核数据集
     * 
     * @param reviewDataset 审核数据集
     * @return 结果
     */
    public int insertReviewDataset(ReviewDataset reviewDataset);

    /**
     * 修改审核数据集
     * 
     * @param reviewDataset 审核数据集
     * @return 结果
     */
    public int updateReviewDataset(ReviewDataset reviewDataset);

    /**
     * 批量删除审核数据集
     * 
     * @param datasetIds 需要删除的审核数据集主键集合
     * @return 结果
     */
    public int deleteReviewDatasetByDatasetIds(Long[] datasetIds);

    /**
     * 删除审核数据集信息
     * 
     * @param datasetId 审核数据集主键
     * @return 结果
     */
    public int deleteReviewDatasetByDatasetId(Long datasetId);
}
