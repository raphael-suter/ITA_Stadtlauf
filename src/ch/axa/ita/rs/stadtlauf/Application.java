package ch.axa.ita.rs.stadtlauf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Application {
    private List<Person> persons;

    public Application() {
        super();

        List<String> startList = getFileAsList("src/ch/axa/ita/rs/stadtlauf/startliste.txt");
        List<String> resultList = getFileAsList("src/ch/axa/ita/rs/stadtlauf/messresultate.txt");

        persons = join(startList, resultList);

        Function<Person, String> outputCategory = person -> person.getRang() + "\t" + person.getNumber() + "\t" + person.getDuration() + "\t" + person.getName();
        Function<Person, String> outputAll = person -> person.getNumber() + "\t" + person.getName() + "\t" + person.getCategory() + "\t" + person.getRang() + "\t" + person.getDuration();

        Arrays.stream(new String[][]{{"1", "junioren"}, {"2", "senioren"}, {"3", "elite"}})
                .forEach(category -> {
                            List<Person> list = getCategory(category[0]);
                            writeListToFile("src/ch/axa/ita/rs/stadtlauf/" + category[1] + ".rl.txt", list, outputCategory);
                        }
                );

        writeListToFile("src/ch/axa/ita/rs/stadtlauf/namen.ref.txt", persons.stream().sorted(Comparator.comparing(Person::getName)).collect(Collectors.toList()), outputAll);
    }

    public List<String> getFileAsList(String file) {
        List<String> data = new ArrayList<>();

        try {
            data = Files.readAllLines(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void writeListToFile(String file, List<Person> data, Function<Person, String> function) {
        List<String> output = data.stream().map(function).collect(Collectors.toList());

        try {
            Files.write(Paths.get(file), output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Person> join(List<String> startList, List<String> resultList) {
        return startList
                .stream()
                .map(startEntry -> {
                    String[] startData = startEntry.split("\t");

                    String result = resultList
                            .stream()
                            .filter(resultEntry -> resultEntry.substring(0, 3).trim().equals(startData[0]))
                            .collect(Collectors.joining());

                    if (result == "") {
                        result = null;
                    } else {
                        result = result.substring(4);
                    }

                    return new Person(startData[0], startData[1], startData[2], result);
                })
                .filter(person -> person.getEnd() != null)
                .collect(Collectors.toList());
    }

    public List<Person> getCategory(String category) {
        return setRang(persons
                .stream()
                .filter(person -> person.getCategory().equals(category))
                .sorted(Comparator.comparing(Person::getDuration))
                .collect(Collectors.toList()));
    }

    public List<Person> setRang(List<Person> persons) {
        IntStream.range(1, persons.size() + 1).forEach(index -> persons.get(index - 1).setRang(index));
        return persons;
    }

    public static void main(String[] args) {
        new Application();
    }
}
