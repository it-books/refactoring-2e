package org.woookk.refactoring.chapter01;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.woookk.refactoring.chapter01.dto.Invoice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class StatementTest {

    @Test
    public void sampleDataTextFormat() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Invoice> invoices = getInvoices(objectMapper);
        Map<String, Map<String, String>> plays = getPlays(objectMapper);

        String format = invoices.stream()
                .map(invoice -> Statement.statement(plays, invoice))
                .collect(Collectors.joining());
        String[] split = format.split("\n");
        assertEquals(6, split.length);
        assertEquals("청구내역(고객명: BigCo)", split[0]);
        assertEquals("  Hamlet: $650.00 (55석)", split[1]);
        assertEquals("  As you like it: $580.00 (35석)", split[2]);
        assertEquals("  Othello: $500.00 (40석)", split[3]);
        assertEquals("총액: $1730.00", split[4]);
        assertEquals("적립 포인트: 47점", split[5]);
    }

    private Map getPlays(ObjectMapper objectMapper) throws IOException {
        return objectMapper.readValue(readFile("plays.json"), Map.class);
    }

    private List<Invoice> getInvoices(ObjectMapper objectMapper) throws IOException {
        return Arrays.asList(objectMapper.readValue(readFile("invoices.json"), Invoice[].class));
    }

    private InputStream readFile(String s) {
        return StatementTest.class.getClassLoader().getResourceAsStream(s);
    }
}