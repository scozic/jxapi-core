package org.jxapi.generator.md.exchange;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.generator.java.JavaCodeGenUtil;

/**
 * Unit test for {@link WriteMdFileSummaryMain}
 */
public class WriteMdFileSummaryMainTest {

    private Path tempDir;

    @Before
    public void setUp() throws IOException {
        tempDir = Files.createTempDirectory("mdtest");
    }

    @After
    public void tearDown() throws IOException {
        // Clean up recursively
      JavaCodeGenUtil.deletePath(tempDir);
    }

    @Test
    public void testWriteSummaryToFile_onDirectory_updatesAllMarkdownFiles() throws Exception {
        // --- Create markdown files ---
        Path md1 = createMarkdownFile("file1.md",
                "# Title 1\n" +
                "<!-- BEGIN TABLE OF CONTENTS -->\n" +
                "<!-- END TABLE OF CONTENTS -->\n" +
                "## Section A\n" +
                "### Sub A1\n");

        Path md2 = createMarkdownFile("file2.md",
                "# Another Title\n" +
                "<!-- BEGIN TABLE OF CONTENTS -->\n" +
                "<!-- END TABLE OF CONTENTS -->\n" +
                "## Part X\n");

        // --- Create a non-md file (should be ignored) ---
        Path txt = Files.writeString(tempDir.resolve("notes.txt"), "Some text");

        // --- Execute ---
        WriteMdFileSummaryMain.writeSummaryToFile(tempDir);

        // --- Validate markdown files were updated ---
        String updated1 = Files.readString(md1);
        String updated2 = Files.readString(md2);
        
        assertEquals(
            "# Title 1\n"
            + "<!-- BEGIN TABLE OF CONTENTS -->\n"
            + "  - [Title 1](#title-1)\n"
            + "    - [Section A](#section-a)\n"
            + "      - [Sub A1](#sub-a1)\n"
            + "\n"
            + "<!-- END TABLE OF CONTENTS -->\n"
            + "## Section A\n"
            + "### Sub A1\n"
            + "",
            updated1);

        assertEquals(
                "# Another Title\n"
                + "<!-- BEGIN TABLE OF CONTENTS -->\n"
                + "  - [Another Title](#another-title)\n"
                + "    - [Part X](#part-x)\n"
                + "\n"
                + "<!-- END TABLE OF CONTENTS -->\n"
                + "## Part X\n"
                + "",
                updated2);
        assertEquals("Some text", Files.readString(txt));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteSummaryToFile_nonExistingFile_throws() throws Exception {
        Path missing = tempDir.resolve("missing.md");
        WriteMdFileSummaryMain.writeSummaryToFile(missing);
    }

    // Helper to create markdown files
    private Path createMarkdownFile(String name, String content) throws IOException {
        Path p = tempDir.resolve(name);
        return Files.writeString(p, content);
    }
}
