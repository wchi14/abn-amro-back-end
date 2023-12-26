package abn.amro100.util;

import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class FileUtils {

    public static List<String> readFileFromResource(String path) throws IOException {
        InputStream is = FileUtils.class.getResourceAsStream(path);
        List<String> strings = new ArrayList<>();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                strings.add(line);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
        return strings;
    }
}
