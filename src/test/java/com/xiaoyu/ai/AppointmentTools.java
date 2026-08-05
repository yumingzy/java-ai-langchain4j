package com.xiaoyu.ai;

import com.xiaoyu.ai.entity.Appointment;
import com.xiaoyu.ai.service.AppointmentService;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;


@SpringBootTest
class AppointmentServiceTest {
@Autowired
private AppointmentService appointmentService;
@Test
void testGetOne() {
    Appointment appointment = new Appointment();
    appointment.setUsername("张三");
    appointment.setIdCard("123456789012345678");
    appointment.setDepartment("内科");
    appointment.setDate("2025-04-14");
    appointment.setTime("上午");
    Appointment appointmentDB = appointmentService.getOne(appointment);
    System.out.println(appointmentDB);
    }
@Test
void testSave() {
    Appointment appointment = new Appointment();
    appointment.setUsername("张三");
    appointment.setIdCard("123456789012345678");
    appointment.setDepartment("内科");
    appointment.setDate("2025-04-14");
    appointment.setTime("上午");
    appointment.setDoctorName("张医生");
    appointmentService.save(appointment);
    }
@Test
void testRemoveById() {
    appointmentService.removeById(1L);
    }


    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;
    @Autowired
    private EmbeddingModel embeddingModel;
    @Test
    public void testUploadKnowledgeLibrary() {
//使用FileSystemDocumentLoader读取指定目录下的知识库文档
//并使用默认的文档解析器对文档进行解析
        Document document1 = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/医院信息.md");
        Document document2 = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/神经内科.md");
        Document document3 = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/科室信息.md");
        List<Document> documents = Arrays.asList(document1, document2, document3);
//文本向量化并存入向量数据库：将每个片段进行向量化，得到一个嵌入向量
        EmbeddingStoreIngestor
                .builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .build()
                .ingest(documents);
    }

    @Test
    public void embeddingSearch() {
        //提问，并将问题转成向量数据
        Embedding queryEmbedding = embeddingModel.embed("神经内科的位置是什么？").content();
        //创建搜索请求对象
        EmbeddingSearchRequest searchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(1) //匹配最相似的一条记录
                //.minScore(0.8)
                .build();

        //根据搜索请求 searchRequest 在向量存储中进行相似度搜索
        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(searchRequest);

        //searchResult.matches()：获取搜索结果中的匹配项列表。
        //.get(0)：从匹配项列表中获取第一个匹配项
        EmbeddingMatch<TextSegment> embeddingMatch = searchResult.matches().get(0);

        //获取匹配项的相似度得分
        System.out.println(embeddingMatch.score()); // 0.8144288515898701

        //返回文本结果
        System.out.println(embeddingMatch.embedded().text());
    }
}