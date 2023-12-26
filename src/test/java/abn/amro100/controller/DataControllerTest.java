package abn.amro100.controller;

import abn.amro100.dto.RecordDto;
import abn.amro100.service.DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DataController.class)
public class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private DataService dataService;

    @BeforeEach()
    public void setup()
    {
        //Init MockMvc Object and build
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testLoadData() throws Exception {
        mockMvc.perform(post("/data"))
                .andExpect(status().isOk())
                .andReturn();

        // Verify that the dataService.inputData() method was called
        verify(dataService).inputData();
    }

    @Test
    public void testGetJSON() throws Exception {
        List<RecordDto> dummyRecords = Arrays.asList(
                getDummyRecordDto(),
                getDummyRecordDto());

        // Mock the dataService.getData() method to return dummyRecords
        when(dataService.getData()).thenReturn(dummyRecords);

        mockMvc.perform(post("/data/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();

        // Verify that the dataService.getData() method was called
        verify(dataService).getData();
    }

    @Test
    public void testGetCSV() throws Exception {
        // Mock the dataService.getCsvData() method to return a dummy CSV response
        byte[] dummyCsvData = "dummy,csv,data".getBytes();
        when(dataService.getCsvData()).thenReturn(outputStream -> outputStream.write(dummyCsvData));

        MvcResult result = mockMvc.perform(post("/data/csv"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=record.csv"))
                .andExpect(content().contentType("text/csv; charset=UTF-8"))
                .andReturn();

        // Verify that the dataService.getCsvData() method was called
        verify(dataService).getCsvData();

        // Verify the content of the response
        assertEquals("dummy,csv,data", result.getResponse().getContentAsString());
    }

    private static RecordDto getDummyRecordDto() {
        return new RecordDto(
                "CL",
                "4321",
                "0003",
                "0001",
                "CME",
                "FU",
                "N1",
                "20100910",
                -79L
        );
    }
}