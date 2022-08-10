package utill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyFileReader {

    public static int countOfGroupsWithMoreOneEl = 0;
    public static int numberOfGroup = 1;
    public static int maxCountOfLines = 1;
    public static Set <Integer> indexOfGroupsWithBiggestCountOfEl = new HashSet<>();

    public static boolean isMatches(String line) {
        // Pattern pattern = Pattern.compile("(\\d+;)+\\d+");
        // Matcher matcher = pattern.matcher(line);
        return line.matches("^(\"\\w*\";)*\"\\w*\"$");
    }

    public static int isLinesInOneGroup(String lastLine, String tempLine) {
        if (lastLine.equals(tempLine)) {
            return 0;
        }
        String[] lastLineArr = lastLine.split(";");
        String[] tempLineArr = tempLine.split(";");
        for (int i = 0; i < lastLineArr.length; i++) {
            if (i < tempLineArr.length && lastLineArr[i].equals(tempLineArr[i])) {
                return 1;
            }
        }
        return -1;
    }

    public static List<String> readFileAndGetVariables(String filePath) {
        List<String> lines = new ArrayList<>();
        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String tempLine = "";

            while ((tempLine = bufferedReader.readLine()) != null && isMatches(tempLine)) {
                lines.add(tempLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    public static List<Set<String>> getGroups(List<String> lines) {
        //лист с группами по индексу
        List<Set<String>> groups = new ArrayList<>();
        //мап   <индекс слова, <слово, группа>> для быстрого поиска в группе ли строка
        HashMap<Integer, HashMap<String, Integer>> numbersByIndexWithGroup = new HashMap<>();

        for (String line : lines) {
            String[] wordsFromLine = line.split(";");
            Integer groupNumber = null;

            //если индекс последней строчки больше чем индекс к слову
            if (numbersByIndexWithGroup.size() < wordsFromLine.length) {
                for(int i = 0; i < wordsFromLine.length; i++){
                    if(i == numbersByIndexWithGroup.size()) numbersByIndexWithGroup.put(i, new HashMap<>());
                }
            }
            //получаем нормер группы, если есть совпадение
            for (int i = 0; i < wordsFromLine.length; i++) {
                String word = wordsFromLine[i];

                //если не существует слов с данным индексом
                if (numbersByIndexWithGroup.size() < wordsFromLine.length) {
                    numbersByIndexWithGroup.put(i, new HashMap<>());
                }

                //получаем мапу со всеми словами, у которых определена группа
                HashMap<String, Integer> numbersInCurrentIndex = numbersByIndexWithGroup.get(i);
                groupNumber = numbersInCurrentIndex.get(word);
                if (groupNumber != null) break;
            }

            //если группа не определена, создаем новую группу, и определяем ее номер
            if (groupNumber == null) {
                groups.add(new HashSet<>());
                groupNumber = groups.size() - 1;
                groups.get(groupNumber).add(line);

            } else {
               Set <String> group = groups.get(groupNumber);
               group.add(line);
               //если количество элементов в группе больше чем максимальное предыдущее количество, перезаписываем индексы, чтобы можно было легко достать из списка
              // if(group.size() > maxSizeOfGroups){
              //     maxSizeOfGroups = group.size();
              //     indexOfGroupsWithBiggestCountOfEl = new ArrayList<>();
              //     indexOfGroupsWithBiggestCountOfEl.add(groupNumber);
              // } else if(group.size() == maxSizeOfGroups){
              //     indexOfGroupsWithBiggestCountOfEl.add(groupNumber);
              // }
            }
            //когда группа создана, добавляем все слова в мапу, чтобы к ней можно было добавлять линии
            for (int i = 0; i < wordsFromLine.length; i++) {
                numbersByIndexWithGroup.get(i).put(wordsFromLine[i], groupNumber);
            }
        }
        //считаем количество элементов с более чем одним элементом и находим группы с наибольшим количеством элементов
        int maxSizeOfGroups = 0;
        for (int i = 0; i < groups.size(); i++) {
            if(groups.get(i).size()>1){
                countOfGroupsWithMoreOneEl++;
            }
            if(groups.get(i).size()> maxSizeOfGroups){
                maxSizeOfGroups = groups.get(i).size();
                indexOfGroupsWithBiggestCountOfEl = new HashSet<>();
                indexOfGroupsWithBiggestCountOfEl.add(i);
            }
            if(groups.get(i).size() == maxSizeOfGroups){
                indexOfGroupsWithBiggestCountOfEl.add(i);
            }
        }
        return groups;
    }

}
