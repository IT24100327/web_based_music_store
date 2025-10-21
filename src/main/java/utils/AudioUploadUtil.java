// In utils/AudioUploadUtil.java
package utils;

import jakarta.servlet.http.Part;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class AudioUploadUtil {

    private static final String UPLOAD_DIR = "uploads";

    public static class AudioUploadResult {
        private final String fullTrackPath;
        private final String snippetPath;

        public AudioUploadResult(String fullTrackPath, String snippetPath) {
            this.fullTrackPath = fullTrackPath;
            this.snippetPath = snippetPath;
        }

        public String getFullTrackPath() {
            return fullTrackPath;
        }

        public String getSnippetPath() {
            return snippetPath;
        }
    }

    public static AudioUploadResult handleAudioUpload(Part filePart, String realPath) throws IOException {
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        File uploadDir = new File(realPath + File.separator + UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        File fullTrackFile = new File(uploadDir, fileName);
        filePart.write(fullTrackFile.getAbsolutePath());

        // Generate Snippet
        String snippetFileName = "snippet_" + fileName;
        File snippetFile = new File(uploadDir, snippetFileName);
        createSnippet(fullTrackFile, snippetFile);

        return new AudioUploadResult(
                UPLOAD_DIR + "/" + fileName,
                UPLOAD_DIR + "/" + snippetFileName
        );
    }

    private static void createSnippet(File source, File target) {
        try {
            AudioAttributes audio = new AudioAttributes();
            // Set audio attributes if needed (e.g., codec, bit rate)

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setAudioAttributes(audio);
            attrs.setDuration(30F); // Create a 30-second snippet

            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (Exception e) {
            e.printStackTrace(); // Handle encoding exception
        }
    }
}