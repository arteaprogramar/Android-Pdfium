package arte.programar.pdfium.exception;

public class PdfException extends RuntimeException {

    public PdfException() {

    }

    public PdfException(String message) {
        super(message);
    }

    public PdfException(Throwable cause) {
        super(cause);
    }

}
