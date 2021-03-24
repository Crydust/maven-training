package com.mycompany.example12.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.util.Objects.requireNonNull;

public enum FileType implements Predicate<byte[]> {
    BMP(new Magic("BM")),
    CLASS(new Magic(new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE})),
    GIF(
            new Magic("GIF87a"),
            new Magic("GIF89a")
    ),
    HEIC(
            new Magic(4, "ftypmif1"),
            new Magic(4, "ftypheic"),
            new Magic(4, "ftypheix"),
            new Magic(4, "ftyphevc"),
            new Magic(4, "ftypheim"),
            new Magic(4, "ftypheis"),
            new Magic(4, "ftyphevm"),
            new Magic(4, "ftyphevs")
    ),
    GZIP(new Magic(new byte[]{(byte) 0X1F, (byte) 0x8B})),
    ICO(new Magic(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00})),
    JPEG(new Magic(new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF})),
    JPEG2000(new Magic(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x6A, (byte) 0x50, (byte) 0x20, (byte) 0x20, (byte) 0x0D, (byte) 0x0A, (byte) 0x87, (byte) 0x0A})),
    JPEGXL(
            new Magic(new byte[]{(byte) 0xFF, (byte) 0x0A}),
            new Magic(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0C, (byte) 0x4A, (byte) 0x58, (byte) 0x4C, (byte) 0x20, (byte) 0x0D, (byte) 0x0A, (byte) 0x87, (byte) 0x0A})
    ),
    MP3(
            new Magic(new byte[]{(byte) 0xFF, (byte) 0xFB}),
            new Magic(new byte[]{(byte) 0xFF, (byte) 0xF3}),
            new Magic(new byte[]{(byte) 0xFF, (byte) 0xF2}),
            new Magic(new byte[]{(byte) 0x49, (byte) 0x44, (byte) 0x33})
    ),
    // doc, xls, ppt, msg
    MS_OFFICE(new Magic(new byte[]{(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0, (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1})),
    OGG(new Magic("OggS")),
    PDF(new Magic(new byte[]{(byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46, (byte) 0x2d})),
    PNG(new Magic(new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A})),
    RTF(new Magic("{\\rtf1")),
    TAR(new Magic(257, "ustar ")),
    TIFF(
            new Magic(new byte[]{(byte) 0x49, (byte) 0x49, (byte) 0x2A, (byte) 0x00}),
            new Magic(new byte[]{(byte) 0x4D, (byte) 0x4D, (byte) 0x00, (byte) 0x2A})
    ),
    WAV(new Magic(
            new MagicPart(0, "RIFF"),
            new MagicPart(8, "WAVE")
    )),
    WEBP(new Magic(
            new MagicPart(0, "RIFF"),
            new MagicPart(8, "WEBP")
    )),
    WMF(new Magic(new byte[]{(byte) 0xD7, (byte) 0xCD, (byte) 0xC6, (byte) 0x9A})),
    XCF(new Magic("gimp xcf")),
    // zip, docx, jar, pptx, xlsx
    ZIP(new Magic(new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04})),
    ZIP_EMPTY(new Magic(new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x05, (byte) 0x06})),
    ZIP_SPANNED(new Magic(new byte[]{(byte) 0x50, (byte) 0x4B, (byte) 0x07, (byte) 0x08}));

    private final Predicate<byte[]> predicate;
    private final int size;

    FileType(Magic... magics) {
        predicate = Arrays.stream((Predicate<byte[]>[]) magics)
                .reduce(it -> false, Predicate::or);
        size = Arrays.stream(magics)
                .mapToInt(Magic::size)
                .max()
                .orElse(0);
    }

    @Override
    public boolean test(byte[] bytes) {
        return predicate.test(bytes);
    }

    private int size() {
        return size;
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
            predicate = Arrays.stream((Predicate<byte[]>[]) parts)
                    .reduce(it -> true, Predicate::and);
            size = Arrays.stream(parts)
                    .mapToInt(MagicPart::size)
                    .max()
                    .orElse(0);
        }

        @Override
        public boolean test(byte[] bytes) {
            return bytes.length >= size()
                    && predicate.test(bytes);
        }

        public int size() {
            return size;
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
                    && Arrays.equals(Arrays.copyOfRange(bytes, offset, offset + magic.length), magic);
        }

        private int size() {
            return offset + magic.length + 1;
        }
    }

    public static Optional<FileType> determineFileType(InputStream in) throws IOException {
        return determineFileType(in, FileType.values());
    }

    public static Optional<FileType> determineFileType(final InputStream in, final FileType... fileTypes) throws IOException {
        requireNonNull(in, "in");
        if (!in.markSupported()) {
            throw new IllegalArgumentException("InputStream doesn't support mark");
        }
        requireNonNull(fileTypes, "fileTypes");
        if (fileTypes.length == 0) {
            throw new IllegalArgumentException("fileTypes is empty");
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

}
