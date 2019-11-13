package com.jz.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;


import java.io.File;

public class IndexManager {
    @Test
    public void addDocument() throws Exception{
        //创建IndexWriter对象，使用IKAnalyzer作为分析起
        IndexWriter indexWriter=new IndexWriter(FSDirectory.open(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\Lucene\\index").toPath()),new IndexWriterConfig(new IKAnalyzer()));
        //创建Document对象
        Document document=new Document();
        //向Document对象中添加域
        document.add(new TextField("name","新添加的文件", Field.Store.YES));
        document.add(new TextField("context","新添加的文件的内容", Field.Store.NO));
        document.add(new StoredField("path","C:/Users/Administrator/Desktop/新建文件夹/Lucene/index"));
        //把文档写入索引库
        indexWriter.addDocument(document);
        //关闭索引
        indexWriter.close();
    }
    @Test
    public void deleteAllDocument()throws Exception{
        IndexWriter indexWriter=new IndexWriter(FSDirectory.open(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\Lucene\\index").toPath()),new IndexWriterConfig(new IKAnalyzer()));
        indexWriter.deleteAll();
        indexWriter.close();
    }
    @Test
    public void deleteDocumentBySelect() throws  Exception{
        IndexWriter indexWriter=new IndexWriter(FSDirectory.open(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\Lucene\\index").toPath()),new IndexWriterConfig(new IKAnalyzer()));
        indexWriter.deleteDocuments(new Term("context","spring"));
        indexWriter.close();
    }
    @Test
    public void update() throws  Exception{
        Document document=new Document();
        document.add(new TextField("name","更新后的文档",Field.Store.YES));
        document.add(new TextField("name2","更新后的文档2",Field.Store.YES));
        document.add(new TextField("name3","更新后的文档3",Field.Store.YES));
        IndexWriter indexWriter=new IndexWriter(FSDirectory.open(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\Lucene\\index").toPath()),new IndexWriterConfig(new IKAnalyzer()));
        indexWriter.updateDocument(new Term("name","spring"),document);
        indexWriter.close();
    }
}
