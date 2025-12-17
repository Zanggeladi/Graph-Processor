-- 官方模型表
drop table if exists tb_official_model;
create table tb_official_model
(
    id             int auto_increment primary key comment '主键id',
    model_name     varchar(255) not null comment '官方模型名称',
#     model_type     varchar(64) not null comment '模型类型（如GCN、GAT等）',
    tags            varchar(255) comment '模型标签（多个标签用逗号分隔）',
    description    text comment '模型描述',
    code_path      varchar(512) not null comment '模型代码存储路径',
    parameters varchar(255) comment '可指定的参数名称列表（多个参数用逗号分隔）',
    datasets    varchar(255) comment '可选的官方数据集列表（多个数据集用逗号分隔）',
    required_files        varchar(255) comment '处理个人数据集用户所需要上传的文件格式列表即描述信息',

    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    remark VARCHAR(500) COMMENT '备注'
#     foreign key (create_by) references `ry-vue`.sys_user(user_name) on delete cascade
) comment '官方模型表';


drop table if exists tb_official_model_task;
create table tb_official_model_task
(
    id               int auto_increment primary key comment '主键id',
    user_id          int not null comment '关联的用户id，引用tb_user(id)',
    official_model_id int not null comment '使用的官方模型id，引用tb_official_model(id)',
    dataset_id       int not null comment '使用的数据集id，引用tb_dataset(id)',
    status           varchar(32) not null comment '任务状态，如Pending, Running, Completed, Failed',
    result_path      varchar(512) comment '任务结果文件路径',

    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
#     foreign key (user_id) references `ry-vue`.sys_user(user_id) on delete cascade,
#     foreign key (official_model_id) references tb_official_model(id) on delete cascade,
#     foreign key (dataset_id) references tb_dataset(id) on delete cascade
) comment '官方模型实验任务表';

drop table if exists tb_official_model_task_result;
create table tb_official_model_task_result
(
    id               int auto_increment primary key comment '主键id',
    task_id          int not null comment '关联的官方模型实验任务id，引用tb_official_model_task(id)',
    metrics          json comment '实验指标（JSON格式，如F1 Score, Precision等）',
    result_file_path varchar(512) comment '结果文件路径',

    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者，关联sys_user表(user_name)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
#     foreign key (task_id) references tb_official_model_task(id) on delete cascade
) comment '官方模型图数据处理任务结果表';

