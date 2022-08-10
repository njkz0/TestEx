package utill;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class MyFileWriter {

    public static void writeTextToFile(String fileName, List<Set<String>> groups) {
        int count = 1;
        StringBuilder text = new StringBuilder();
        text.append("Число групп с больше чем одной линией: ").append(MyFileReader.countOfGroupsWithMoreOneEl);

        try (FileWriter writer = new FileWriter(fileName, true);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(text + "\n");
            bufferedWriter.flush();
            //Сверху группы с наибольшим количеством
            if (!MyFileReader.indexOfGroupsWithBiggestCountOfEl.isEmpty()) {
                for (int index: MyFileReader.indexOfGroupsWithBiggestCountOfEl){
                    text = stringBuilderOfGroup(groups.get(index), count);
                    bufferedWriter.write(text.toString());
                    bufferedWriter.flush();
                    count ++;
                }
            }
            //все остальные группы
            for (int i = 0; i < groups.size(); i++) {
                if(MyFileReader.indexOfGroupsWithBiggestCountOfEl.contains(i)) continue;
                text = stringBuilderOfGroup(groups.get(i), count);
                bufferedWriter.write(text.toString());
                bufferedWriter.flush();
                count ++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StringBuilder stringBuilderOfGroup(Set<String> group, int index) {
        StringBuilder text = new StringBuilder();
        text.append("Группа " + index + "\n");
        for(String line: group){
            text.append(line + "\n");
        }
        return text;
    }
}
