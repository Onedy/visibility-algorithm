package com.inditex.visibilityalgorithm.core.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvOperations {
    public static List<String[]> readCsv(String filename) throws IOException, CsvException {
        try (Reader reader = Files.newBufferedReader(readPath(filename));
             CSVReader csvReader = new CSVReader(reader)) {
            return csvReader.readAll();
        }
    }

    private static Path readPath(String filename) throws IOException {
        return new ClassPathResource(filename).getFile().toPath();
    }
}
