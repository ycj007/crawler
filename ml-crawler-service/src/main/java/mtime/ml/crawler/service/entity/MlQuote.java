package mtime.ml.crawler.service.entity;


import lombok.Data;
import lombok.ToString;
import mtime.lark.db.jsd.NameStyle;
import mtime.lark.db.jsd.annotation.JsdTable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@ToString
@JsdTable(nameStyle = NameStyle.LOWER)
public class MlQuote {
    @Id
    @GeneratedValue
    private int id;
    // url+content hash
    private String hash;
    private String url;
    //基本信息
    //内容
    private String content;
    //Home Authors Topics
    private String category;
    //关联的关键词
    private String keywords;
    //作者
    private String author;
   //=======其他信息===============================
    //topic
    private String topic;

    //作者的其他信息 职业 //国籍 生日
    // 主题的内容
    private String info;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;










}
