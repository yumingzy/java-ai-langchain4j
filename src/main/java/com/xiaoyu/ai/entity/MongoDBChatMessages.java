package com.xiaoyu.ai.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
// @AllArgsConstructor：自动生成包含所有成员变量的全参构造函数
@AllArgsConstructor
// @NoArgsConstructor：自动生成无参构造函数
@NoArgsConstructor
// @Data：自动生成 getter、setter、toString、equals 和 hashCode 方法
@Data
// 作用：将当前实体类映射到 MongoDB 数据库中名为 "chat_messages" 的集合（Collection）
@Document(collection = "chat_messages")
public class MongoDBChatMessages {
    private Object messageId;
    private String memoryId;
    private String content;
}
