import utill.MyFileReader;
import utill.MyFileWriter;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");
        String filePath = userDir + separator + "src" + separator + "files" + separator + "lng.csv";
        String resultPath = userDir + separator + "src" + separator + "files" + separator + "result.txt";
        //получаем все линии, кроме неподходящих по формату
        List<String> lines= MyFileReader.readFileAndGetVariables(filePath);
        //получаем список с группами
        List<Set<String>> list = MyFileReader.getGroups(lines);
        //записываем в файл
        MyFileWriter.writeTextToFile(resultPath, list);
    }
}
