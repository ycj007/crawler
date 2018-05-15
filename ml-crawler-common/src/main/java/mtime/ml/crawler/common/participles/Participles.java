package mtime.ml.crawler.common.participles;

import com.chenlb.mmseg4j.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;

public class Participles {


    public static String participlesWithmseg4j(String originString) {
        Objects.requireNonNull(originString, "输入不可为空");
        StringBuilder sb = new StringBuilder();
        Dictionary dic = Dictionary.getInstance();
        Seg seg = new ComplexSeg(dic);
        Word word = null;
        try (Reader reader = new StringReader(originString);) {
            MMSeg mmSeg = new MMSeg(reader, seg);
            while ((word = mmSeg.next()) != null) {

                sb.append(word);
                System.out.println(word.getString());
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }

    }

    public static String participlesWithIK(String originString) {
        Objects.requireNonNull(originString, "输入不可为空");
        StringBuilder sb = new StringBuilder();
        try (
                StringReader reader = new StringReader(originString);
                //创建分词对象
                Analyzer anal = new IKAnalyzer(true);
        ) {

            //分词
            TokenStream ts = anal.tokenStream("", reader);

            CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
            //遍历分词数据
            while (ts.incrementToken()) {
                sb.append(term.toString());
                System.out.print(term.toString() + "|");
            }
            return sb.toString();

        } catch (Exception e) {
            return null;
        }

    }

    public static void main(String[] args) {
        String str = " 一、 如果爱，请深爱；如果相爱，请相爱一辈子！\n" +
                "\n" +
                " 二、 明天我们上街一群人碰到一对就开唱“分手快乐”.\n" +
                "\n" +
                " 三、 等有人让你不孤单的时候，放下手中的烟，忘记曾经伤与痛！\n" +
                "\n" +
                " 四、 你得声音又在我耳边想起、让我没法抗拒你给的温柔\n" +
                "\n" +
                " 五、 你以为最酸的感觉是吃醋吗？不是，最酸的感觉是没权吃醋。\n" +
                "\n" +
                " 六、 所有的深情，原来是由许多细碎的时光一一串成的，就像一串亮着迷蒙微光的小灯泡，静静地俯伏在脚边，照亮着我们彼此相依相伴的身影。\n" +
                "\n" +
                " 七、 极端的人，还是比较快乐的，他们才没有那么多的心理挣扎。\n" +
                "\n" +
                " 八、 男人口中的理想的，就是个彻尾的蠢女人。\n" +
                "\n" +
                " 九、 倾我一生一世念，来如飞花散似烟。\n" +
                "\n" +
                " 十、 贱人没有矫情，贱人就是贱*\n" +
                "\n" +
                " 十一、 此去经年，悲欢离合变，转瞬沧海桑田。\n" +
                "\n" +
                " 十二、 在你曾经爱过我的那些短暂岁月里，我或许是世界上最幸福的人，只是那些日子已成过去，要留也留不住。\n" +
                "\n" +
                " 十三、 以上进奋斗为荣，以无所事事为耻。\n" +
                "\n" +
                " 十四、 当爱情缺席的时候，你要努力些，努力工作，努力让自己进步。男人有了事业，便有女人。女人有了事业，即便没有爱情，至少还有钱。\n" +
                "\n" +
                " 十五、 身为一道彩虹雨过了就该闪亮整片天空。\n" +
                "\n" +
                " 十六、 握得太紧，东西会碎，手会痛 。.\n" +
                "\n" +
                "\n" +
                "\n" +
                " 十七、 失败是学习过程中的必经阶段，你若未曾失败过，只能说明你承受的风险还不够。\n" +
                "\n" +
                " 十八、 我是你转身就忘的路人甲，凭什么陪你蹉跎年华到天涯？\n" +
                "\n" +
                " 十九、 \"如果你快乐不是因为我,当放手了彼此是不是会更幸福\"\n" +
                "\n" +
                " 二十、 纵使分离、此后各有怀抱。但终究曾是那个陪我横渡时间之河的人。他在我心中、翻起过波澜、永不会流逝。\n" +
                "\n" +
                " 二十一、 感情上，我是个非常小心眼的人，我不能容忍我喜欢的人对别的女生比对我好，我亦不能容忍他对别的女生玩暧昧！\n" +
                "\n" +
                " 二十二、 尘封的记忆，春天的来临，悄悄释岀暧昧的气息。\n" +
                "\n" +
                " 二十三、 说我现实、曾经我比你还天真。\n" +
                "\n" +
                " 二十四、 爱上你我却把最初旳自己给丢了。\n" +
                "\n" +
                " 二十五、 叶散的时候，明白欢聚；花谢的时候，明白青春。\n" +
                "\n" +
                " 二十六、 以后你们别对任何人说我爱你，因为他们不会信，也别相信任何人说他爱你！\n" +
                "\n" +
                " 二十七、 人在荆棘中，不动不刺；心在俗世中，不动不伤。\n" +
                "\n" +
                " 二十八、 一些人，仅仅想念，不联系。\n" +
                "\n" +
                " 二十九、 人生苦难重重。这是个伟大的真理，是世界上最伟大的真理之一。\n" +
                "\n" +
                " 三十、 如果说，我真的只能站在你的背后，收藏你的喜怒哀乐，那么我也会觉得很知足，至少我是在你身边的！\n" +
                "\n" +
                " 三十一、 你本来很爱一个人，可是，当所有的失望累积到了一个临界点，连爱也再提不起劲了。\n" +
                "\n";

        try {
            participlesWithmseg4j(str);
            System.out.println("-----------------------------------------------");
            participlesWithIK(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
