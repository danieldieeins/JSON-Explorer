package live.nerotv;

import com.formdev.flatlaf.FlatDarkLaf;
import live.nerotv.json_explorer.APIExplorer;
import live.nerotv.shademebaby.logger.Logger;

import javax.swing.*;

public class Main {

    private static Logger logger;
    private static String apiKey;
    private static APIExplorer explorer;

    public static void main(String[] a) {
        logger = new Logger("JSON-Explorer");
        logger.log("JSON-Explorer by nerotvlive: https://a.nerotv.live");
        if(resolveArguments(a)) {
            logger.log("Starting JSON-Explorer...");
            try {
                FlatDarkLaf.setup();
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } catch (Exception e) {
                logger.error("Couldn't set UIManager's LookAndFeel to dark FlatLaf: "+e.getMessage());
            }
            explorer = new APIExplorer(apiKey);
            apiKey = null;
        }
    }

    private static boolean resolveArguments(String[] args) {
        logger.log("Resolving arguments...");
        for(int i = 0; i < args.length; i++) {
            String argument = args[i];
            if(argument.equalsIgnoreCase("--api-key")) {
                try {
                    apiKey = args[i+1];
                } catch (Exception e) {
                    logger.error("You need to specify an api key!");
                    return false;
                }
            }
        }
        if(apiKey==null) {
            apiKey = "";
        }
        return true;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setAPIKey(String newApiKey) {
        explorer.setAPIKey(newApiKey);
    }
}