package hello.jdbc.repository.ex;

/**
 *  MyDbException을 상속받아서 의미있는 계층을 형성한다. 이렇게하면 데이터베이스 관련 예외라는 계층을 만들 수 있다.
 *  이 예외는 데이터 중복의 경우에만 던져야 한다.
 */

public class MyDuplicationKeyException extends MyDbException {

    public MyDuplicationKeyException() {
    }

    public MyDuplicationKeyException(String message) {
        super(message);
    }

    public MyDuplicationKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicationKeyException(Throwable cause) {
        super(cause);
    }
}
