package abn.amro100.controller;

import abn.amro100.constant.UrlConstant;
import abn.amro100.dto.RecordDto;
import abn.amro100.exception.InvalidSourceFileException;
import abn.amro100.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = UrlConstant.URL_DATA)
@RequiredArgsConstructor
@Slf4j
public class DataController {

    private final DataService dataService;

    @PostMapping
    public ResponseEntity<String> loadData() throws IOException, InvalidSourceFileException {
        log.info("Going to load data ...");
        dataService.inputData();
        return ResponseEntity.ok("OK");
    }

    @PostMapping(UrlConstant.URL_GET_JSON_DATA)
    public ResponseEntity<List<RecordDto>> getJSON() {
        log.info("Going to get data in JSON format ...");
        return ResponseEntity.ok(dataService.getData());
    }

    @PostMapping(UrlConstant.URL_GET_CSV_DATA)
    public ResponseEntity<StreamingResponseBody> getCSV() {
        log.info("Going to get data in CSV format ...");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s", "record.csv"))
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(dataService.getCsvData());
    }

}
