import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
    public static void main(String[] args) throws IOException, InterruptedException {

        File folder = new File("C:\\UNVphoto\\upload");
        File[] listOfFiles = folder.listFiles();
        int i = 0;
        int y = 1;
        String personcode = "0000";
        String patternStr = "[0-9]";
        while (i < listOfFiles.length) {
            String json;
            String filePath1 = listOfFiles[i].toString();
            String filePath2 = listOfFiles[i + 1].toString();
            byte[] fileContent1 = FileUtils.readFileToByteArray(new File(filePath1));
            byte[] fileContent2 = FileUtils.readFileToByteArray(new File(filePath2));
            String base64_1 = Base64.getEncoder().encodeToString(fileContent1);
            String sizeBase64_1 = String.valueOf(base64_1.length());
            String base64_2 = Base64.getEncoder().encodeToString(fileContent2);
            String sizeBase64_2 = String.valueOf(base64_2.length());
            long unixTimestamp = Instant.now().getEpochSecond();
            if(y <= 9)
            {
                personcode = "000"+ String.valueOf(y);
            }
            if(y <= 99 && y > 9)
            {
                personcode = "00"+ String.valueOf(y);
            }
            if(y <= 999 && y > 99)
            {
                personcode = "0"+ String.valueOf(y);
            }
            System.out.println(personcode);

            String personname = listOfFiles[i].getName().substring(0, 20).substring(0, listOfFiles[i].getName().substring(0, 20).lastIndexOf(' '));
            System.out.println(personname);
            json = String.format("{\n" +
                    "  \"Num\": 1,\n" +
                    "  \"PersonInfoList\": [\n" +
                    "    {\n" +
                    "      \"PersonID\": 0,\n" +
                    "      \"LastChange\": %s,\n" +
                    "      \"PersonCode\": \"%s\",\n" +
                    "      \"PersonName\": \"%s\",\n" +
                    "      \"TimeTemplateNum\": 0,\n" +
                    "      \"IdentificationNum\": 2,\n" +
                    "      \"IdentificationList\": [\n" +
                    "        {\n" +
                    "          \"Type\": 99,\n" +
                    "          \"Number\": \"\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"Type\": 99,\n" +
                    "          \"Number\": \"\"\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"ImageNum\": 2,\n" +
                    "      \"ImageList\": [\n" +
                    "        {\n" +
                    "          \"FaceID\": 0,\n" +
                    "          \"Size\": %s,\n" +
                    "          \"Type\": 2,\n" +
                    "          \"LastChange\": %s,\n" +
                    "          \"Data\": \"%s\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"FaceID\": 0,\n" +
                    "          \"Size\": %s,\n" +
                    "          \"Type\": 2,\n" +
                    "          \"LastChange\": %s,\n" +
                    "          \"Data\": \"%s\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}", unixTimestamp, personcode, personname, sizeBase64_1, unixTimestamp, base64_1, sizeBase64_2, unixTimestamp, base64_2);
            URL url = new URL("http://172.16.22.10/LAPI/V1.0/PeopleLibraries/1619414560/People");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response);
            }
            i = i + 2;
            y++;
        }
    }
}