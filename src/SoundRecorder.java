import javax.sound.sampled.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SoundRecorder {
    private final String DATE_FORMAT_PATTERN = "yyyyMMdd_HHmmss";

    private AudioFileFormat.Type fileType;
    private TargetDataLine line;
    private SimpleDateFormat dateFormat;
    private FileUploader fileUploader;

    SoundRecorder(FileUploader fileUploader) {
        this.fileUploader = fileUploader;
        DataLine.Info info = new DataLine.Info(
                TargetDataLine.class,
                getAudioFormat()
        );

        try {
            this.line = (TargetDataLine) AudioSystem.getLine(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        this.fileType = AudioFileFormat.Type.WAVE;
    }

    private AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public void recordAudio(int milliseconds) {
        String fileName = dateFormat.format(new Date()) + ".wav";
        File file = new File(fileName);
        start(file);
        stop(milliseconds, file);
    }

    private void start(File file) {
        new Thread(() -> {
            try {
                line.open(getAudioFormat());
                line.start();
                AudioInputStream ais = new AudioInputStream(line);
                AudioSystem.write(ais, fileType, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void stop(int milliseconds, File file) {
        new Thread(() -> {
            try {
                Thread.sleep(milliseconds);
                line.stop();
                line.close();
                recordAudio(milliseconds);
                fileUploader.uploadFile(file);
                // TODO: 24.06.2020 delete file
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
