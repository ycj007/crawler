package mtime.ml.crawler.service.entity;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GexingsModel {


    //当前位置 当前位置: 个性说说 > 经典说说 >
    private String place;
    //标题  qq空间说说带图片 唯美心情说说
    private String title;
    //额外信息  时间:2018-03-13 17:31来源:个性说说网 作者:
    private String info;
    //具体内容
    private String content;
}
