package mtime.ml.crawler.common.participles;

import com.chenlb.mmseg4j.*;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

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


    public static void  runAllAnnotators(String doc) {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = doc;//"this is a simple text"; // Add your text here!

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        parserOutput(document);
    }

    public static void parserOutput(Annotation document) {
        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);


        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);

                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                System.out.println(ne);
            }

            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            System.out.println("语法树：");
            System.out.println(tree.toString());

            // this is the Stanford dependency graph of the current sentence
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
            System.out.println("依存句法：");
            System.out.println(dependencies.toString());
        }

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
        Map<Integer, CorefChain> graph =
                document.get(CorefCoreAnnotations.CorefChainAnnotation.class);

    }


    public static void main(String[] args) {
        String str = "When PredictionIO bumps a version, it creates another JAR file with the new version number.";

       /* try {
            participlesWithmseg4j(str);
            System.out.println("-----------------------------------------------");
            participlesWithIK(str);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        runAllAnnotators(str);
    }

}
