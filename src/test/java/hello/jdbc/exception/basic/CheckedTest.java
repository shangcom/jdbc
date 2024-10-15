package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class CheckedTest {

    @Test
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throw(){
        Service service = new Service();
        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MycheckedException.class);
    }

    /* Exception을 상속받은 예외는 체크 예외가 된다.*/
    static class MycheckedException extends Exception {
        public MycheckedException(String message) {
            super(message);
        }
    }

    /**
     * Checked 예외는 처리하거나 던지거나 둘 중 하나 강제됨.
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MycheckedException e) {
                //예외 처리 로직
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * 체크예외를 밖으로 던지는 코드
         * throw 메서드 필수
         */
        public void callThrow() throws MycheckedException {
            repository.call();
        }
    }

    static class Repository {
        public void call() throws MycheckedException {
            throw new MycheckedException("ex");
        }
    }

}
