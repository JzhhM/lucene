package com.jz.lucene;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;


import java.io.File;

public class LuceneFirst {
    @Test
    public void createIndex() throws Exception{
//        1.创建Director对象，指定索引库保存的位置
        //把索引库保存到内存中
        //Directory directory=new RAMDirectory();
        //把所有保存到磁盘
        Directory directory= FSDirectory.open(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\Lucene\\index").toPath());
//        2.基于DIrectory对象创建一个IndexWriter对象
        IndexWriter indexWriter=new IndexWriter(directory,new IndexWriterConfig());
//        3.读取磁盘上的文件，对应每个文件创建一个文档对象
        File dir =new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\Lucene\\searchsource");
        File[] files = dir.listFiles();
        for (File f: files){
            String filename=f.getName();   //获取文件名
            String filePath=f.getPath();   //获取文件内容
            String fileContext=FileUtils.readFileToString(f,"UTF-8"); //获取文件内容
            long fileSize=FileUtils.sizeOf(f); //获取文件大小
            //创建Filed;
            //参数1：域的名字，参数2：域的内容，参数3：是否存储
            Field fieldName=new TextField("name",filename,Field.Store.YES);
            Field fieldPath=new TextField("path",filePath,Field.Store.YES);
            Field fieldContext=new TextField("context",fileContext,Field.Store.YES);
            Field fieldSize=new TextField("size",fileSize+"",Field.Store.YES);
            //创建文档对象
            Document document =new Document();
            // 4.向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContext);
            document.add(fieldSize);
            //5.把文档对象写入索引库
            indexWriter.addDocument(document);
        }
//        6.关闭indexwriter对象
        indexWriter.close();
    }
    @Test
    public void serchIndex() throws Exception{
//        1.创建Director对象，指定索引库的位置
        Directory directory= FSDirectory.open(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\Lucene\\index").toPath());
//        2.创建IndexReader对象
        IndexReader indexReader= DirectoryReader.open(directory);
//        3.创建IndexSearcher对象，构造方法中的参数IndexReader对象。
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);
//        4.创建一个Query对象，TermQuery
        //参数1： 域  参数2：关键词
        Query query=new TermQuery(new Term("context","spring"));
//        5.执行查询，得到一个TopDocs对象
        //参数1 ：查询对象  参数2：查询结果返回的最大记录数
        indexSearcher.search(query,10);
//        6.取查询结果的总记录数
        TopDocs topDocs=indexSearcher.search(query,10);
//        7.取文档列表
        System.out.println("查询总记录数"+topDocs.totalHits);
//        8.打印文档中的内容
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc doc:scoreDocs){
            //文档ID
            int docId=doc.doc;
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
            System.out.println(document.get("context"));
            System.out.println("------------------------------");

        }
//        9.关闭indexReader对象
        indexReader.close();
    }
    @Test
    public void testTokenStream() throws Exception{
//        1.创建一个Analyzer对象，StandardAnalyzer对象
        Analyzer analyzer=new StandardAnalyzer();
//        2.使用分析器对象的tokenStream方法获得一个TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("", "The Spring Framework provides a comprehensive programming and configuration model.");
//        3.向TokenStream对象中设置一个引用
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
//        4.调用TokenSteam对象的reset方法。
        tokenStream.reset();
//        5.使用while循环遍历，TokenSteam对象
        while(tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
//        6.关闭
        tokenStream.close();
    }
    @Test
    public void createIKaNALYZERIndex() throws Exception{
//        1.创建Director对象，指定索引库保存的位置
        //把索引库保存到内存中
        //Directory directory=new RAMDirectory();
        //把所有保存到磁盘
        Directory directory= FSDirectory.open(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\Lucene\\index").toPath());
//        2.基于DIrectory对象创建一个IndexWriter对象
        IndexWriter indexWriter=new IndexWriter(directory,new IndexWriterConfig(new IKAnalyzer()));
//        3.读取磁盘上的文件，对应每个文件创建一个文档对象
        File dir =new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\Lucene\\searchsource");
        File[] files = dir.listFiles();
        for (File f: files){
            String filename=f.getName();   //获取文件名
            String filePath=f.getPath();   //获取文件内容
            String fileContext=FileUtils.readFileToString(f,"UTF-8"); //获取文件内容
            long fileSize=FileUtils.sizeOf(f); //获取文件大小
            //创建Filed;
            //参数1：域的名字，参数2：域的内容，参数3：是否存储
            Field fieldName=new TextField("name",filename,Field.Store.YES);
            //Field fieldPath=new TextField("path",filePath,Field.Store.YES);
            Field fieldPath=new StoredField("path",filePath);
            Field fieldContext=new TextField("context",fileContext,Field.Store.YES);
            Field fileSizeValue=new LongPoint("size",fileSize);
            Field fieldSizeStore=new StoredField("size",fileSize);
            //创建文档对象
            Document document =new Document();
            // 4.向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContext);
           // document.add(fieldSize);
            document.add(fileSizeValue);
            document.add(fieldSizeStore);
            //5.把文档对象写入索引库
            indexWriter.addDocument(document);
        }
//        6.关闭indexwriter对象
        indexWriter.close();
    }
}
