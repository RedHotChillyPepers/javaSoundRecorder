import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        Configurations configurations = new Configurations();
        try {
            Configuration configuration = configurations.properties(new File("/dropbox.properties"));
            FileUploader fileUploader = new DropboxFileUploader(configuration);
            SoundRecorder recorder = new SoundRecorder(fileUploader);
            recorder.recordAudio(30000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
