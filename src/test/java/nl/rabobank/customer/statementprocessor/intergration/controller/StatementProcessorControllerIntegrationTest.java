package nl.rabobank.customer.statementprocessor.intergration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class StatementProcessorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldUploadCSVStatementFileSuccessfullyWithDuplicateReference() throws Exception {

        String csvContent = """
                Reference,AccountNumber,Description,Start Balance,Mutation,End Balance
                194261,NL91RABO0315273637,Clothes from Jan Bakker,21.6,-41.83,-20.23
                112806,NL27SNSB0917829871,Clothes for Willem Dekker,91.23,+15.57,106.8
                183049,NL69ABNA0433647324,Clothes for Jan King,86.66,+44.5,131.16
                183356,NL74ABNA0248990274,Subscription for Peter de Vries,92.98,-46.65,46.33
                112806,NL69ABNA0433647324,Clothes for Richard de Vries,90.83,-10.91,79.92
                112806,NL93ABNA0585619023,Tickets from Richard Bakker,102.12,+45.87,147.99
                139524,NL43AEGO0773393871,Flowers from Jan Bakker,99.44,+41.23,140.67
                179430,NL93ABNA0585619023,Clothes for Vincent Bakker,23.96,-27.43,-3.47
                141223,NL93ABNA0585619023,Clothes from Erik Bakker,94.25,+41.6,135.85
                195446,NL74ABNA0248990274,Flowers for Willem Dekker,26.32,+48.98,75.3
            """;
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        mockMvc.perform(multipart("/api/v1/statements")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())  // HTTP 200
                .andExpect(jsonPath("$.reportId").exists())  // Check the reportId
                .andExpect(jsonPath("$.failedRecords").exists())
                .andExpect(jsonPath("$.failedRecords.size()").value(3)) ;// Check if there are no failed records
    }

    @Test
     void shouldUploadCSVStatementFileSuccessfullyWithNoValidationErrors() throws Exception {

        String csvContent = """
                Reference,AccountNumber,Description,Start Balance,Mutation,End Balance
                194261,NL91RABO0315273637,Clothes from Jan Bakker,21.6,-41.83,-20.23
                183049,NL69ABNA0433647324,Clothes for Jan King,86.66,+44.5,131.16
                183356,NL74ABNA0248990274,Subscription for Peter de Vries,92.98,-46.65,46.33
                112806,NL69ABNA0433647324,Clothes for Richard de Vries,90.83,-10.91,79.92
                139524,NL43AEGO0773393871,Flowers from Jan Bakker,99.44,+41.23,140.67
                179430,NL93ABNA0585619023,Clothes for Vincent Bakker,23.96,-27.43,-3.47
            """;
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        mockMvc.perform(multipart("/api/v1/statements")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())  // HTTP 200
                .andExpect(jsonPath("$.reportId").exists())  // Check the reportId
                .andExpect(jsonPath("$.failedRecords").isEmpty());
    }

    @Test
    void shouldUploadXMLStatementFileSuccessfullyWithInvalidBalance() throws Exception {

        String csvContent = """
                <records>
                   <record reference="130498">
                     <accountNumber>NL69ABNA0433647324</accountNumber>
                     <description>Tickets for Peter Theuß</description>
                     <startBalance>26.9</startBalance>
                     <mutation>-18.78</mutation>
                     <endBalance>8.12</endBalance>
                   </record>
                   <record reference="167875">
                     <accountNumber>NL93ABNA0585619023</accountNumber>
                     <description>Tickets from Erik de Vries</description>
                     <startBalance>5429</startBalance>
                     <mutation>-939</mutation>
                     <endBalance>6368</endBalance>
                   </record>
                   <record reference="147674">
                     <accountNumber>NL93ABNA0585619023</accountNumber>
                     <description>Subscription from Peter Dekker</description>
                     <startBalance>74.69</startBalance>
                     <mutation>-44.91</mutation>
                     <endBalance>29.78</endBalance>
                   </record>
                 </records>
            """;
        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", csvContent.getBytes());

        mockMvc.perform(multipart("/api/v1/statements")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())  // HTTP 200
                .andExpect(jsonPath("$.reportId").exists())  // Check the reportId
                .andExpect(jsonPath("$.failedRecords").exists())
                .andExpect(jsonPath("$.failedRecords.size()").value(1)) ;// Check if there are no failed records
    }

    @Test
    void shouldUploadXMLStatementFileSuccessfullyWithNoValidationErrors() throws Exception {

        String csvContent = """
                <records>
                   <record reference="130498">
                     <accountNumber>NL69ABNA0433647324</accountNumber>
                     <description>Tickets for Peter Theuß</description>
                     <startBalance>26.9</startBalance>
                     <mutation>-18.78</mutation>
                     <endBalance>8.12</endBalance>
                   </record>
                   <record reference="147674">
                     <accountNumber>NL93ABNA0585619023</accountNumber>
                     <description>Subscription from Peter Dekker</description>
                     <startBalance>74.69</startBalance>
                     <mutation>-44.91</mutation>
                     <endBalance>29.78</endBalance>
                   </record>
                 </records>
            """;
        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", csvContent.getBytes());

        mockMvc.perform(multipart("/api/v1/statements")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())  // HTTP 200
                .andExpect(jsonPath("$.reportId").exists())  // Check the reportId
                .andExpect(jsonPath("$.failedRecords").isEmpty());
    }

    @Test
    void shouldFailToUploadAnInvalidXMLStatementFile() throws Exception {

        String csvContent = """
                 <records>
                   <record reference="130498">
                     <accountNumber>NL69ABNA0433647324</accountNumber>
                     <description>Tickets for Peter Theu</description>
                     <startBalance>26.9</startBalance>
            """;
        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", csvContent.getBytes());

        mockMvc.perform(multipart("/api/v1/statements")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())  // HTTP 400
                .andExpect(jsonPath("$.error").value("Error parsing the file"));
    }

    @Test
    void shouldFailToUploadAnInvalidCSVStatementFile() throws Exception {

        String csvContent = """
                Reference,AccountNumber,Description,Start Balance,Mutation,End Balance
                wwww,NL91RABO0315273637,Clothes from Jan Bakker,21.6,www.83,ttt
            """;
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        mockMvc.perform(multipart("/api/v1/statements")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest()) // HTTP 400
                .andExpect(jsonPath("$.error").value("Error parsing the file"));
    }
    @Test
     void shouldReturn400ForInvalidFile() throws Exception {

        MockMultipartFile invalidFile = new MockMultipartFile("file", "invalid.txt", "text/plain", "invalid data".getBytes());

        mockMvc.perform(multipart("/api/v1/statements")
                        .file(invalidFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())  // HTTP 400
                .andExpect(jsonPath("$.details").value("Unsupported file type"));
    }
}