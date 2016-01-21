package bolona_pig.proj_imgapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by DreamMii on 21/1/2559.
 */
public class HttpRequest {

    public HttpRequest() {
    }

    protected String makeHttpRequest(Map<String, String> data, String address) {

        String encodeData = getEncodeData(data);

        BufferedReader reader = null; // Read some data from server
        String line = "";

        try {
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(encodeData);
            writer.flush();

            StringBuilder strb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));


            while ((line = reader.readLine()) != null) {
                strb.append(line + "\n");
            }
            line = strb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close(); // Close Reader
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return line;
    }

    private String getEncodeData(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (sb.length() > 0)
                sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }
}
