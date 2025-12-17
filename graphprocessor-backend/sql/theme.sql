DROP TABLE IF EXISTS tb_theme;

CREATE TABLE tb_theme (
    theme_id INT AUTO_INCREMENT PRIMARY KEY,
    theme_name VARCHAR(255) NOT NULL COMMENT '主题名称',
    description TEXT COMMENT '主题描述',
    status ENUM('draft', 'active', 'inactive') DEFAULT 'draft' COMMENT '主题状态',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否活跃',
    priority INT DEFAULT 0 COMMENT '优先级',
    last_accessed_time DATETIME COMMENT '最后访问时间',
    experiment_count INT DEFAULT 0 COMMENT '实验数量',
    tags VARCHAR(255) COMMENT '标签',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
)
    COMMENT '对比实验主题表';