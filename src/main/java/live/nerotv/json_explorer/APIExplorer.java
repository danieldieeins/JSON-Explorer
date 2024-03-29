package live.nerotv.json_explorer;

import com.google.gson.*;
import live.nerotv.Main;
import live.nerotv.json_explorer.utils.IOUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class APIExplorer {

    private ExplorerFrame frame;
    private String apiKey;

    public APIExplorer(String apiKey) {
        this.apiKey = apiKey;
        SwingUtilities.invokeLater(()->{
            frame = new ExplorerFrame();
            frame.initialise(this);
            frame.setSize(1000,700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public void setAPIKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @SuppressWarnings("all")
    private String formatJson(String input) {
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(input).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }

    private URI encode(String input) {
        try {
            URI uri = URI.create(input);

            String query = uri.getQuery();
            String path = uri.getPath();
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = uri.getPort();
            return new URI(scheme, null, host, port, path, query, null);
        } catch (Exception e) {
            return null;
        }
    }

    public void makeRequest(String urlString) {
        frame.getButton().setText("Making request...");
        frame.getButton().setEnabled(false);
        try {
            frame.getOutputArea().setText("Resolving...");
            if(urlString.startsWith("http")) {
                URI uri = encode(urlString);
                URL url = uri.toURL();
                String urlString_ = urlString.toLowerCase().replace("https://", "").replace("http://", "");
                if (urlString_.startsWith("api.curseforge.com")) {
                    frame.getOutputArea().setText(formatJson(resolveCurseforgeRequest(url)));
                } else {
                    frame.getOutputArea().setText(formatJson(resolveRequest(url)));
                }
            } else {
                String urlString_ = urlString.replace("file:///", "").replace("file://", "");
                frame.getOutputArea().setText(resolveLocalRequest(urlString_));
            }
            frame.getButton().setText("Make request");
            frame.getButton().setEnabled(true);
        } catch (Exception e) {
            Main.getLogger().error("Bad request: "+e.getMessage());
            frame.getOutputArea().setText("Please input a valid URL to make a request.");
            frame.getButton().setText("Make request");
            frame.getButton().setEnabled(true);
        }
    }

    private String resolveRequest(URL url) {
        Main.getLogger().log("Resolving request "+url+"...");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Accept", "application/json");
            return IOUtils.getContent(connection.getInputStream());
        } catch (Exception e) {
            String error = "Couldn't resolve request: "+e.getMessage();
            Main.getLogger().error(error);
            return error;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    private String resolveLocalRequest(String path) {
        try {
            File file = new File(URLDecoder.decode(path,StandardCharsets.UTF_8));
            Reader reader = new FileReader(file.getAbsolutePath());
            JsonElement json = JsonParser.parseReader(reader);
            return formatJson(json.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String resolveCurseforgeRequest(URL url) {
        Main.getLogger().log("Resolving CurseForge request "+url+"...");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("x-api-key", apiKey);
            return IOUtils.getContent(connection.getInputStream());
        } catch (Exception e) {
            String error = "Couldn't resolve CurseForge request: "+e.getMessage();
            Main.getLogger().error(error);
            return error;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}