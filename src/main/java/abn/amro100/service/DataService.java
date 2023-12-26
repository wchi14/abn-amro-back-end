package abn.amro100.service;

import abn.amro100.dto.RecordDto;
import abn.amro100.exception.InvalidSourceFileException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;

public interface DataService {

    void inputData() throws IOException, InvalidSourceFileException;

    List<RecordDto> getData();

    StreamingResponseBody getCsvData();
}
