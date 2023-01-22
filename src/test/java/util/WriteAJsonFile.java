package util;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class WriteAJsonFile {
    public void createAndWriteInFile(String[][] values, String directory, String fileName) {

        // Creating a JSONObject object
        JSONObject jsonObject = new JSONObject();
        // Inserting key-value pairs into the json object
        for (String[] value : values) {
            jsonObject.put(value[0], value[1]);
        }
        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                boolean isDirCreated = dir.mkdirs();
                if (isDirCreated) {
                    log.info("Directory Created");
                }
            }
            File file = new File(directory + "/" + fileName);
            boolean isFileCreated = file.createNewFile();
            if (isFileCreated) {
                log.info("File Created");
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
