package hello.jdbc.repository.ex;


/**
 * 데이터베이스에서 발생한 오류를 처리하기 위한 사용자 정의 예외.
 */
public class MyDbException extends RuntimeException {
    public MyDbException() {
    }

    public MyDbException(String message) {
        super(message);
    }

    public MyDbException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDbException(Throwable cause) {
        super(cause);
    }
}
