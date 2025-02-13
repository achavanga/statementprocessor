package nl.rabobank.customer.statementprocessor.controller;

import nl.rabobank.customer.statementprocessor.boundary.controller.StatementProcessorController;
import nl.rabobank.customer.statementprocessor.boundary.dto.Report;
import nl.rabobank.customer.statementprocessor.control.exception.InvalidFileException;
import nl.rabobank.customer.statementprocessor.control.service.StatementProcessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(StatementProcessorController.class)
class StatementProcessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatementProcessorService processorService;  // Mock the service

    @Test
    void shouldUploadStatementFileSuccessfully() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "data".getBytes());
        Report report = new Report(1L, List.of());  // Mock the report response
        when(processorService.process(file)).thenReturn(report);  // Mock service behavior

        mockMvc.perform(multipart("/api/v1/statements")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())  // HTTP 200
                .andExpect(jsonPath("$.reportId").value(1L))  // Check the reportId
                .andExpect(jsonPath("$.failedRecords").isEmpty());  // Check if there are no failed records
    }

    @Test
    void shouldReturn400ForInvalidFile() throws Exception {

        MockMultipartFile invalidFile = new MockMultipartFile("file", "invalid.txt", "text/plain", "invalid data".getBytes());
        when(processorService.process(invalidFile)).thenThrow(new InvalidFileException("Invalid file format"));

        mockMvc.perform(multipart("/api/v1/statements")
                        .file(invalidFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())  // HTTP 400
                .andExpect(jsonPath("$.error").value("Invalid file"))  // Check for 'error' key
                .andExpect(jsonPath("$.details").value("Invalid file format"));  // Check for 'details' key
    }
}