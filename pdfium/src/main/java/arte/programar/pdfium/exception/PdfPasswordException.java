package arte.programar.pdfium.exception;

public class PdfPasswordException extends RuntimeException {

    public PdfPasswordException() {
    }

    public PdfPasswordException(String message) {
        super(message);
    }

    public PdfPasswordException(Throwable cause) {
        super(cause);
    }

}
