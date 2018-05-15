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
public class MlGexings {


    @Id
    @GeneratedValue
    private int id;
    //当前位置 当前位置: 个性说说 > 经典说说 >
    private String place;
    //标题  qq空间说说带图片 唯美心情说说
    private String title;
    //额外信息  时间:2018-03-13 17:31来源:个性说说网 作者:
    private String info;
    //具体内容
    private String content;
    //分过词的内容
    private String resolveContent;
    //页面url
    private String url;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}

