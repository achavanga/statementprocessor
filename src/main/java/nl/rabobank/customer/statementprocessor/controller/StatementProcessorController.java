package nl.rabobank.customer.statementprocessor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import nl.rabobank.customer.statementprocessor.dto.Report;
import nl.rabobank.customer.statementprocessor.dto.response.ErrorResponse;
import nl.rabobank.customer.statementprocessor.service.StatementProcessorService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static nl.rabobank.customer.statementprocessor.util.StatementConstants.CUSTOMER_STATEMENT_PROCESSOR_API;
import static nl.rabobank.customer.statementprocessor.util.StatementConstants.CUSTOMER_STATEMENT_TAG;

/**
 * REST controller responsible for processing customer statements.
 * This controller provides endpoints for handling customer statement files.
 */
@RestController
@RequestMapping("/api/v1/statements")
@Tag(name = CUSTOMER_STATEMENT_TAG, description = CUSTOMER_STATEMENT_PROCESSOR_API)
public class StatementProcessorController {

    private final StatementProcessorService processorService;

    public StatementProcessorController(StatementProcessorService processorService) {
        this.processorService = processorService;
    }

    @Operation(summary = "Upload customer statement. Supports CSV and XML formats.",
            description = "This endpoint allows you to upload customer statement files in CSV or XML format. " +
                    "The system will process the uploaded file and return a report with the validation results."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer statement processed successfully.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Report.class),
                            examples = @ExampleObject(value = """
                                    {"reportId": 7091186762284010000,"failedRecords": []}
                                    """))),
            @ApiResponse(responseCode = "400", description = "Bad request due to invalid file format or processing error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"error": Invalid file format,"details": The uploaded file is not in a supported format (CSV or XML).}
                                    """))),
            @ApiResponse(responseCode = "415", description = "Unsupported media type, only CSV and XML file formats are allowed.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"error": Unsupported file format,"details": Only CSV and XML formats are supported.}
                                    """))),
            @ApiResponse(responseCode = "500", description = "Internal server error. Something went wrong during processing.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {"error": File processing failed,"details": Unexpected error occurred during file processing. Please try again later.
                                    }"""))),
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Report> processStatementUpload(@RequestParam("file") MultipartFile file) {
        var report = processorService.process(file);
        return ResponseEntity.ok(report);
    }

}
