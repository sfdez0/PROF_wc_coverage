package es.upm.grise.profundizacion.wc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppTest {

    private static Path testFile = Paths.get("ejemplo.txt");

    @BeforeAll
    public static void setup() throws IOException {
        Files.writeString(testFile,
                "kjdbvws wonvwofjw\n sdnfwijf ooj    kjndfohwouer 21374 vehf\n jgfosj\n\nskfjwoief ewjf\n\n\ndkfgwoihgpw vs wepfjwfin");
    }

    @AfterAll
    public static void teardown() {
        try {
            Files.deleteIfExists(testFile);
        } catch (IOException e) {
            System.err.println("Error deleting test file: " + e.getMessage());
            try {
                Thread.sleep(100);
                Files.deleteIfExists(testFile);
            } catch (IOException | InterruptedException ex) {
                System.err.println("Failed to delete test file on retry: " + ex.getMessage());
            }
        }
    }

    /**
     * Test that when no arguments are provided, the usage message is printed.
     */
    @Test
    public void testUsageMessageWhenNoArgs() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        App.main(new String[] {});

        assertEquals("Usage: wc [-clw file]\n".trim(), output.toString().trim());
    }

    /**
     * Test that when more than two arguments are provided, an error message is printed.
     */
    @Test
    public void testParametersCount() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        App.main(new String[] { "abc", "abc", "abc" });

        assertEquals("Wrong arguments!", output.toString().trim());
    }

    /**
     * Test that when a non-existent file is provided, an error message is printed.
     */
    @Test
    public void testWrongFile() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        String filename = "nonexistentfile.txt";
        App.main(new String[] { "-c", filename });

        assertEquals("Cannot find file: " + filename, output.toString().trim());
    }

    /**
     * Test that when commands do not start with a dash, an error message is printed.
     */
    @Test
    public void testCommandsStartWithDash() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        App.main(new String[] { "c", "ejemplo.txt" });

        assertEquals("The commands do not start with -", output.toString().trim());
    }

    /**
     * Test that when unrecognized commands are provided, an error message is printed.
     */
    @Test
    public void testWrongCommands() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        App.main(new String[] { "-clwa", "ejemplo.txt" });

        assertEquals("Unrecognized command: a", output.toString().trim());
    }

    /**
     * Test that the correct counts are printed for characters, lines, and words.
     */
    @Test
    public void testPrintResults() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        App.main(new String[] { "-clw", "ejemplo.txt" });

        int c = 109;
        int l = 7;
        int w = 20;

        assertEquals(c + "\t" + l + "\t" + w + "\tejemplo.txt", output.toString().trim());
    }
}
