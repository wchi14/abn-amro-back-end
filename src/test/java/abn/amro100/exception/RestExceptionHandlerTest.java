package abn.amro100.exception;

import abn.amro100.constant.Constant;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RestExceptionHandlerTest {

    @InjectMocks
    private RestExceptionHandler restExceptionHandler;

    @Mock
    private HttpHeaders headers;

    @Mock
    private WebRequest webRequest;

    @Test
    public void testHandleInvalidSourceFileException() {
        InvalidSourceFileException exception = new InvalidSourceFileException();
        ResponseEntity<Object> responseEntity = restExceptionHandler.handleInvalidSourceFileException(exception, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(Constant.ERROR_MESSAGE_INVALID_SOURCE_FILE, responseEntity.getBody());
        assertTrue(responseEntity.getHeaders().isEmpty());
    }

    @Test
    public void testHandleGeneralException() {
        Exception exception = new Exception("General exception");
        ResponseEntity<Object> responseEntity = restExceptionHandler.handleGeneralException(exception, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("General exception", responseEntity.getBody());
        assertTrue(responseEntity.getHeaders().isEmpty());
    }
}