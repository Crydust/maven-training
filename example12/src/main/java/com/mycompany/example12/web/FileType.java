package com.mycompany.example12.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
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
            new Magic(hex("CAFEBABE"))
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
            new Magic(hex("1F8B"))
    ),
    ICO(
            Pattern.compile("(?i).*\\.ico"),
            new Magic(hex("00000100"))
    ),
    JPEG(
            Pattern.compile("(?i).*\\.jpe?g"),
            new Magic(hex("FFD8FFDB")),
            new Magic(
                    new MagicPart(hex("FFD8FFE0")),
                    new MagicPart(6, "JFIF")
            ),
            new Magic(hex("FFD8FFEE")),
            new Magic(
                    new MagicPart(hex("FFD8FFE1")),
                    new MagicPart(6, "Exif")
            )
    ),
    JPEG2000(
            Pattern.compile("(?i).*\\.(?:jp2|j2k|jpf|jpm|jpg2|j2c|jpc|jpx|mj2)"),
            new Magic(hex("0000000C6A5020200D0A870A"))
    ),
    JPEGXL(
            Pattern.compile("(?i).*\\.jxl"),
            new Magic(hex("FF0A")),
            new Magic(hex("0000000C4A584C200D0A870A"))
    ),
    MP3(
            Pattern.compile("(?i).*\\.mp3"),
            new Magic(hex("FFFB")),
            new Magic(hex("FFF3")),
            new Magic(hex("FFF2")),
            new Magic(hex("494433"))
    ),
    MS_OFFICE(
            Pattern.compile("(?i).*\\.(?:doc|xls|ppt|msg)"),
            new Magic(hex("D0CF11E0A1B11AE1"))
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
            new Magic(hex("89504E470D0A1A0A"))
    ),
    RTF(
            Pattern.compile("(?i).*\\.rtf"),
            new Magic("{\\rtf1")
    ),
    TIFF(
            Pattern.compile("(?i).*\\.tiff?"),
            new Magic(hex("49492A00")),
            new Magic(hex("4D4D002A"))
    ),
    WAV(
            Pattern.compile("(?i).*\\.wav"),
            new Magic(
                    new MagicPart("RIFF"),
                    new MagicPart(8, "WAVE")
            )
    ),
    WEBP(
            Pattern.compile("(?i).*\\.webp"),
            new Magic(
                    new MagicPart("RIFF"),
                    new MagicPart(8, "WEBP")
            )
    ),
    WMF(
            Pattern.compile("(?i).*\\.wmf"),
            new Magic(hex("D7CDC69A"))
    ),
    XCF(
            Pattern.compile("(?i).*\\.xcf"),
            new Magic("gimp xcf")
    ),
    ZIP(
            Pattern.compile("(?i).*\\.(?:zip|aar|apk|docx|epub|ipa|jar|kmz|maff|odp|ods|odt|pk3|pk4|pptx|usdz|vsdx|xlsx|xpi)"),
            new Magic(hex("504B0304"))
    ),
    ZIP_EMPTY(
            Pattern.compile("(?i).*\\.(?:zip|aar|apk|docx|epub|ipa|jar|kmz|maff|odp|ods|odt|pk3|pk4|pptx|usdz|vsdx|xlsx|xpi)"),
            new Magic(hex("504B0506"))
    ),
    ZIP_SPANNED(
            Pattern.compile("(?i).*\\.(?:zip|aar|apk|docx|epub|ipa|jar|kmz|maff|odp|ods|odt|pk3|pk4|pptx|usdz|vsdx|xlsx|xpi)"),
            new Magic(hex("504B0708"))
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
        return bytes.length >= this.size
                && this.predicate.test(bytes);
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

        private MagicPart(String magic) {
            this(0, magic);
        }

        private MagicPart(int offset, String magic) {
            this(offset, magic.getBytes(US_ASCII));
        }

        private MagicPart(byte[] magic) {
            this(0, magic);
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

    private static byte[] hex(String s) {
        Objects.requireNonNull(s, "s");
        if (s.length() == 0) {
            return new byte[0];
        }
        if (s.length() % 2 != 0) {
            throw new IllegalArgumentException("String length must be even");
        }
        final int length = s.length() / 2;
        final byte[] val = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = i * 2;
            val[i] = (byte) (Integer.parseInt(s.substring(index, index + 2), 16) & 0xFF);
        }
        return val;
    }

    private static Optional<FileType> determineFileType(final InputStream in) throws IOException {
        return determineFileType(in, FileType.values());
    }

    private static Optional<FileType> determineFileType(final InputStream in, final String fileName) throws IOException {
        return determineFileType(in, determineFileTypeByFileName(fileName));
    }

    private static FileType[] determineFileTypeByFileName(String fileName) {
        return Arrays.stream(FileType.values())
                .filter(it -> it.fileNameMatches(fileName))
                .toArray(FileType[]::new);
    }

    private static Optional<FileType> determineFileType(final InputStream in, final FileType... fileTypes) throws IOException {
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
