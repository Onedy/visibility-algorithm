package com.inditex.visibilityalgorithm.controller;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

public class ResourcesUtils {
    public static String readResource(String filename) throws IOException {
        return new String(Files.readAllBytes(new ClassPathResource(filename).getFile().toPath()));
    }
}
