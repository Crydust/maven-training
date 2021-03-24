package com.mycompany.example12.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.Objects.requireNonNull;

public enum FileType implements Predicate<byte[]> {
    BMP(
            Pattern.compile("(?i).*\\.bmp"),
            new Magic("BM")
    ),
    CLASS(
            Pattern.compile("(?i).*\\.class"),
            new Magic(new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE})
    ),
    GIF(
            Pattern.compile("(?i).*\\.gif"),
            new Magic("GIF87a"),
            new Magic("GIF89a")
    ),
    HEIC(
            Pattern.compile("(?i).*\\.(?:heif|heifs|heic|heics|avci|avcs|avif|avifs)"),
            new Magic(4, "ftypmif1"),
            new Magic(4, "ftypheic"),
            new Magic(4, "ftypheix"),
            new Magic(4, "ftyphevc"),
            new Magic(4, "ftypheim"),
            new Magic(4, "ftypheis"),
            new Magic(4, "ftyphevm"),
            new Magic(4, "ftyphevs")
    ),
    GZIP(
            Pattern.compile("(?i).*\\.gz"),
            new Magic(new byte[]{(byte) 0X1F, (byte) 0x8B})
    ),
    ICO(
            Pattern.compile("(?i).*\\.ico"),
            new Magic(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00})
    ),
    JPEG(
            Pattern.compile("(?i).*\\.jpe?g"),
            new Magic(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xDB}),
            new Magic(
                    new MagicPart(0, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0}),
                    new MagicPart(6, "JFIF")
            ),
            new Magic(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xEE}),
            new Magic(
                    new MagicPart(0, new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE1}),
                    new MagicPart(6, "Exif")
            )
    ),
    JPEG2000(
            Pattern.compile("(?i).*\\.(?:jp2|j2k|jpf|jpm|jpg2|j2c|jpc|jpx|mj2)"),
            new Magic(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x6A, (byte) 0x50, (byte) 0x20, (byte) 0x20, (byte) 0x0D, (byte) 0x0A, (byte) 0x87, (byte) 0x0A})
    ),
    JPEGXL(
            Pattern.compile("(?i).*\\.jxl"),
            new Magic(new byte[]{(byte) 0xFF, (byte) 0x0A}),
            new Magic(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x4A, (byte) 0x58, (byte) 0x4C, (byte) 0x20, (byte) 0x0D, (byte) 0x0A, (byte) 0x87, (byte) 0x0A})
    ),
    MP3(
            Pattern.compile("(?i).*\\.mp3"),
            new Magic(new byte[]{(byte) 0xFF, (byte) 0xFB}),
            new Magic(new byte[]{(byte) 0xFF, (byte) 0xF3}),
            new Magic(new byte[]{(byte) 0xFF, (byte) 0xF2}),
            new Magic(new byte[]{(byte) 0x49, (byte) 0x44, (byte) 0x33})
    ),
    MS_OFFICE(
            Pattern.compile("(?i).*\\.(?:doc|xls|ppt|msg)"),
            new Magic(new byte[]{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1})
    ),
    OGG(
            Pattern.compile("(?i).*\\.og[gav]"),
            new Magic("OggS")
    ),
    PDF(
            Pattern.compile("(?i).*\\.pdf"),
            new Magic("%PDF-")
    ),
    PNG(
            Pattern.compile("(?i).*\\.png"),
            new Magic(new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A})
    ),
    RTF(
            Pattern.compile("(?i).*\\.rtf"),
            new Magic("{\\rtf1")
    ),
    TIFF(
            Pattern.compile("(?i).*\\.tiff?"),
            new Magic(new byte[]{(byte) 0x49, (byte) 0x49, (byte) 0x2A, (byte) 0x00}),
            new Magic(new byte[]{(byte) 0x4D, (byte) 0x4D, (byte) 0x00, (byte) 0x2A})
    ),
    WAV(
            Pattern.compile("(?i).*\\.wav"),
            new Magic(
                    new MagicPart(0, "RIFF"),
                    new MagicPart(8, "WAVE")
            )
    ),
    WEBP(
            Pattern.compile("(?i).*\\.webp"),
            new Magic(
                    new MagicPart(0, "RIFF"),
                    new MagicPart(8, "WEBP")
            )
    ),
    WMF(
            Pattern.compile("(?i).*\\.wmf"),
            new Magic(new byte[]{(byte) 0xD7, (byte) 0xCD, (byte) 0xC6, (byte) 0x9A})
    ),
    XCF(
            Pattern.compile("(?i).*\\.xcf"),
            new Magic("gimp xcf")
    ),
    ZIP(
            Pattern.compile("(?i).*\\.(?:zip|aar|apk|docx|epub|ipa|jar|kmz|maff|odp|ods|odt|pk3|pk4|pptx|usdz|vsdx|xlsx|xpi)"),
            new Magic(new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04})
    ),
    ZIP_EMPTY(
            Pattern.compile("(?i).*\\.(?:zip|aar|apk|docx|epub|ipa|jar|kmz|maff|odp|ods|odt|pk3|pk4|pptx|usdz|vsdx|xlsx|xpi)"),
            new Magic(new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x05, (byte) 0x06})
    ),
    ZIP_SPANNED(
            Pattern.compile("(?i).*\\.(?:zip|aar|apk|docx|epub|ipa|jar|kmz|maff|odp|ods|odt|pk3|pk4|pptx|usdz|vsdx|xlsx|xpi)"),
            new Magic(new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x07, (byte) 0x08})
    );

    private final Pattern fileNamePattern;
    private final Predicate<byte[]> predicate;
    private final int size;

    FileType(Pattern fileNamePattern, Magic... magics) {
        this.fileNamePattern = fileNamePattern;
        this.predicate = Arrays.stream((Predicate<byte[]>[]) magics)
                .reduce(it -> false, Predicate::or);
        this.size = Arrays.stream(magics)
                .mapToInt(Magic::size)
                .max()
                .orElse(0);
    }

    public boolean fileNameMatches(String fileName) {
        return this.fileNamePattern.matcher(fileName).matches();
    }

    @Override
    public boolean test(byte[] bytes) {
        return this.predicate.test(bytes);
    }

    private int size() {
        return this.size;
    }

    private static final class Magic implements Predicate<byte[]> {

        private final Predicate<byte[]> predicate;
        private final int size;

        private Magic(byte[] magic) {
            this(new MagicPart(0, magic));
        }

        private Magic(String magic) {
            this(new MagicPart(0, magic));
        }

        private Magic(int offset, byte[] magic) {
            this(new MagicPart(offset, magic));
        }

        private Magic(int offset, String magic) {
            this(new MagicPart(offset, magic));
        }

        public Magic(MagicPart... parts) {
            this.predicate = Arrays.stream((Predicate<byte[]>[]) parts)
                    .reduce(it -> true, Predicate::and);
            this.size = Arrays.stream(parts)
                    .mapToInt(MagicPart::size)
                    .max()
                    .orElse(0);
        }

        @Override
        public boolean test(byte[] bytes) {
            return bytes.length >= this.size
                    && this.predicate.test(bytes);
        }

        public int size() {
            return this.size;
        }
    }

    private static final class MagicPart implements Predicate<byte[]> {
        private final int offset;
        private final byte[] magic;

        private MagicPart(int offset, String magic) {
            this(offset, magic.getBytes(US_ASCII));
        }

        private MagicPart(int offset, byte[] magic) {
            this.offset = offset;
            this.magic = magic;
        }

        @Override
        public boolean test(byte[] bytes) {
            return bytes.length >= size()
                    && Arrays.equals(Arrays.copyOfRange(bytes, this.offset, this.offset + this.magic.length), this.magic);
        }

        private int size() {
            return this.offset + this.magic.length + 1;
        }
    }

    public static Optional<FileType> determineFileType(final InputStream in) throws IOException {
        return determineFileType(in, FileType.values());
    }

    public static Optional<FileType> determineFileType(final InputStream in, final String fileName) throws IOException {
        return determineFileType(in, determineFileTypeByFileName(fileName));
    }

    public static FileType[] determineFileTypeByFileName(String fileName) {
        return Arrays.stream(FileType.values())
                .filter(it -> it.fileNameMatches(fileName))
                .toArray(FileType[]::new);
    }

    public static Optional<FileType> determineFileType(final InputStream in, final FileType... fileTypes) throws IOException {
        requireNonNull(in, "in");
        if (!in.markSupported()) {
            throw new IllegalArgumentException("InputStream doesn't support mark");
        }
        requireNonNull(fileTypes, "fileTypes");
        if (fileTypes.length == 0) {
            // fileTypes is empty
            return Optional.empty();
        }
        final int longestMagic = Arrays.stream(fileTypes)
                .mapToInt(FileType::size)
                .max()
                .orElse(0);
        if (longestMagic == 0) {
            throw new IllegalStateException("No usable magics");
        }
        in.mark(longestMagic);
        final byte[] bytes = new byte[longestMagic];
        final int read = in.read(bytes);
        in.reset();
        if (read < 1) {
            // The InputStream is empty
            return Optional.empty();
        }
        return Arrays.stream(fileTypes)
                .filter(it -> it.size() <= read)
                .filter(it -> it.test(bytes))
                .findAny();
    }

    public static boolean fileTypeAndFileNameLikelyMatch(final InputStream in, final String fileName) throws IOException {
        final FileType[] fileTypes = determineFileTypeByFileName(fileName);
        // we don't recognize this file name so we assume it isn't an imposter
        if (fileTypes.length == 0) {
            return true;
        }
        // the magic bytes match the file name
        return determineFileType(in, fileTypes).isPresent();
    }
}
