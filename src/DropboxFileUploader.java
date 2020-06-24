import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.apache.commons.configuration2.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DropboxFileUploader implements FileUploader {
    private final String CLIENT_IDENTIFIER_KEY = "client_identifier";
    private final String ACCESS_TOKEN_KEY = "access_token";

    private final DbxClientV2 client;
    private final Configuration configuration;

    public DropboxFileUploader(Configuration configuration) {
        this.configuration = configuration;
        DbxRequestConfig config = DbxRequestConfig
                .newBuilder(configuration.getString(CLIENT_IDENTIFIER_KEY))
                .withAutoRetryEnabled()
                .build();
        this.client = new DbxClientV2(config, configuration.getString(ACCESS_TOKEN_KEY));
    }

    @Override
    public void uploadFile(File file) {
        try (InputStream in = new FileInputStream(file)) {
            client.files().uploadBuilder("/" + file.getName()).uploadAndFinish(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
