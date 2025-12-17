-- 数据集表
DROP TABLE IF EXISTS tb_dataset;
CREATE TABLE tb_dataset (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dataset_name VARCHAR(255) NOT NULL COMMENT '数据集名称',
    description TEXT COMMENT '数据集描述',
    tags    varchar(255) comment '标签（多个标签用逗号分隔）',
    dataset_path VARCHAR(1024) NOT NULL COMMENT '数据集存储路径',
    size BIGINT DEFAULT 0 COMMENT '数据集大小（字节数）',
    version INT DEFAULT 1 COMMENT '版本号',
    status TINYINT DEFAULT 0 COMMENT '数据集状态：0-draft, 1-pending, 2-publiched, 3-archived, 4-retracted, 5-deleted',
    isDelete tinyint default 0 not null comment '；逻辑删除：0-正常，1-删除',

    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
#     foreign key (create_by) references `ry-vue`.sys_user(user_name) on delete cascade
)
    COMMENT '数据集表';

-- 模型表
drop table if exists tb_model;
CREATE TABLE tb_model (
    id INT AUTO_INCREMENT comment '模型ID' PRIMARY KEY,
    model_name VARCHAR(255) NOT NULL COMMENT '模型名称',
    tags            varchar(255) comment '模型标签（多个标签用逗号分隔）',
    description     text comment '模型描述',

    #      model_code LONGBLOB NOT NULL COMMENT '模型代码',

    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
#     foreign key (create_by) references sys_user(user_name) on delete cascade
)
    comment '模型表';

-- 模型代码表
drop table if exists tb_model_code;
create table tb_model_code
(
    id              int auto_increment primary key comment '主键id',
    model_id        int not null comment '关联的模型id，引用tb_model表(id)',
    version         varchar(32) not null comment '代码版本号',
    description     text comment '代码描述和说明',
    code text NOT NULL COMMENT '模型代码',

    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
#     foreign key (model_id) references tb_model(id) on delete cascade,
#     foreign key (create_by) references sys_user(user_name) on delete cascade
) comment '模型代码表';

-- 对比实验主题表
DROP TABLE IF EXISTS tb_experiment_theme;
create table tb_experiment_theme
(
    id              int auto_increment primary key comment '主键id',
    theme_name      varchar(255) not null comment '对比实验主题名称',
    description     text comment '对比实验主题描述',

    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者，关联sys_user表(user_name)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
#     foreign key (create_by) references sys_user(user_name) on delete cascade

) comment '对比实验主题表';

-- 对比实验任务表
DROP TABLE IF EXISTS tb_experiment_task;
create table tb_experiment_task
(
    id              int auto_increment primary key comment '主键id',
    task_name       varchar(255) not null comment '对比实验任务名称',
    description     text comment '对比实验任务描述',
    theme_id        int not null comment '关联的对比实验主题id，引用tb_comparison_theme表(id)',
    dataset_id      int not null comment '关联的数据集id，引用tb_dataset表(id)',
    model_id        int not null comment '关联的模型id，引用tb_model表(id)',
    hyperparameters json comment '实验超参数（JSON格式）',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '对比实验任务状态：0-PENDING, 1-RUNNING, 2-COMPLETED, 3-FAILED',

    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者，关联sys_user表(user_name)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
#     foreign key (create_by) references sys_user(user_name) on delete cascade,
#     foreign key (theme_id) references tb_experiment_theme(id) on delete cascade,
#     foreign key (dataset_id) references tb_dataset(id) on delete cascade,
#     foreign key (model_id) references tb_model(id) on delete cascade
) comment '对比实验任务表';

drop table if exists tb_experiment_result;
create table tb_experiment_result(
    id              int auto_increment primary key comment '主键id',
    task_id         int not null comment '关联的实验任务id，引用tb_experiment_task表(id)',
    metrics         json comment '实验指标（JSON格式，如F1 Score, Precision等）',
    result_file_path varchar(512) comment '结果文件路径',

    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者，关联sys_user表(user_name)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
#     foreign key (task_id) references tb_experiment_task(id) on delete cascade
) comment '实验结果表';

