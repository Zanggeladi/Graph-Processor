create table tb_model
(
    id          int auto_increment comment '模型ID'
        primary key,
    model_name  varchar(255)                          not null comment '模型名称',
    tags        varchar(255)                          null comment '模型标签（多个标签用逗号分隔）',
    description text                                  null comment '模型描述',
    user_id         int                                   not null comment '关联的用户id，引用sys_user表(id)',
    create_by   varchar(64) default ''                null comment '创建者',
    create_time datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_by   varchar(64) default ''                null comment '更新者',
    update_time datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    remark      varchar(500)                          null comment '备注'
)
    comment '模型表';

create table tb_dataset
(
    id   int auto_increment
        primary key,
    dataset_name varchar(255)                          not null comment '数据集名称',
    description  text                                  null comment '数据集描述',
    tags         varchar(255)                          null comment '标签',
    dataset_path varchar(1024)                         not null comment '数据集存储路径',
    size         bigint      default 0                 null comment '数据集大小（字节数）',
    version      int         default 1                 null comment '版本号',
    status       tinyint     default 0                 null comment '数据集状态：0-draft, 1-published, 2-archived',
    user_id         int                                   not null comment '关联的用户id，引用sys_user表(id)',
    create_by    varchar(64) default ''                null comment '创建者',
    create_time  datetime    default CURRENT_TIMESTAMP null comment '创建时间',
    update_by    varchar(64) default ''                null comment '更新者',
    update_time  datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    remark       varchar(500)                          null comment '备注'
)
    comment '数据集表';

