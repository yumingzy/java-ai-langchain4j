package com.xiaoyu.ai.store;

import com.xiaoyu.ai.entity.MongoDBChatMessages;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;


import java.util.LinkedList;
import java.util.List;

@Component
public class MongoDBChatMemoryStore implements ChatMemoryStore {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        Criteria criteria=Criteria.where("memoryId").is(memoryId);
        Query query=new Query(criteria);
        MongoDBChatMessages mongoDBChatMessages = mongoTemplate.findOne(query, MongoDBChatMessages.class);
        if (mongoDBChatMessages==null) return new LinkedList<>();
        return ChatMessageDeserializer.messagesFromJson(mongoDBChatMessages.getContent());
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        Criteria criteria=Criteria.where("memoryId").is(memoryId);
        Query query=new Query(criteria);
        Update update=new Update();
        update.set("content", ChatMessageSerializer.messagesToJson(messages));
        mongoTemplate.upsert(query,update,MongoDBChatMessages.class);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        Criteria criteria=Criteria.where("memoryId").is(memoryId);
        Query query=new Query(criteria);
        mongoTemplate.remove(query,MongoDBChatMessages.class);
    }
}
