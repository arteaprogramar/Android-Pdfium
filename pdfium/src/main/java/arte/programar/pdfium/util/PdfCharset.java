package arte.programar.pdfium.util;

import android.os.Build;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PdfCharset {

    public static Charset getCharset() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return StandardCharsets.UTF_16LE;
        } else {
            return Charset.forName("UTF-16LE");
        }
    }

}
