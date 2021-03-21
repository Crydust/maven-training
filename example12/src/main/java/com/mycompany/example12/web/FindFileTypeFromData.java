package com.mycompany.example12.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FindFileTypeFromData {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/Users/kristof/Pictures/avatar.jpg");
        try (BufferedInputStream in = new BufferedInputStream(Files.newInputStream(path))) {
            System.out.println("determineFileType = " + FileType.determineFileType(in).orElse(null));
            System.out.println("isAnImage = " + isAnImage(in));
        }
    }

    private static boolean isAnImage(InputStream in) throws IOException {
        return FileType.determineFileType(
                in,
//                FileType.BMP, FileType.GIF, FileType.ICO, FileType.JPEG, FileType.PNG, FileType.TIFF, FileType.WEBP
                FileType.GIF, FileType.JPEG, FileType.PNG
        ).isPresent();
    }

}
