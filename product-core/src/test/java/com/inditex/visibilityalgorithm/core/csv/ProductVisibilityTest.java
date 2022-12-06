package com.inditex.visibilityalgorithm.core.csv;

import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.inditex.visibilityalgorithm.core.csv.CsvOperations.readCsv;
import static org.assertj.core.api.Assertions.assertThat;

class ProductVisibilityTest {
    @Test
    void shouldReturnVisibleProductsOnlyOrderedBySequenceField() throws IOException, CsvException {
        List<String[]> productCsv = readCsv("default-data/product.csv");
        List<String[]> sizeCsv = readCsv("default-data/size.csv");
        List<String[]> stockCsv = readCsv("default-data/stock.csv");

        assertThat(ProductVisibility.visibleIds(productCsv, sizeCsv, stockCsv))
            .isEqualTo("5,1,3");
    }
}