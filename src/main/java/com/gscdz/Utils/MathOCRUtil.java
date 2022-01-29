package com.gscdz.Utils;

import cc.chungkwong.mathocr.common.EncodedExpression;
import cc.chungkwong.mathocr.common.format.LatexFormat;
import cc.chungkwong.mathocr.offline.extractor.Extractor;
import cc.chungkwong.mathocr.online.recognizer.MyscriptRecognizer;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * @author Hu
 * @date 2021-09-19 15:58
 * @description: get the characters from the images
 */
@Slf4j
public class MathOCRUtil {
    private static final String applicationKey="1526f528-7d79-440f-91e8-0b4453419fe1";
    private static final String hmacKey="9165b016-e2eb-4354-92b0-4f860b8ab8d5";
    private static final String grammarId="gscdz";
    private static final int dpi=96;

    public static String getMathCodeFromImage(File file){
        MyscriptRecognizer myscriptRecognizer=new MyscriptRecognizer(applicationKey,hmacKey,grammarId,dpi);
        Extractor extractor=new Extractor(myscriptRecognizer);
        String latexCode=null;
        try {
            EncodedExpression expression=extractor.recognize(ImageIO.read(file));
            latexCode=expression.getCodes(new LatexFormat());
            log.info(latexCode);
        } catch (IOException e) {
            log.error(e.toString(),e);
        }
        return latexCode;
    }
}
