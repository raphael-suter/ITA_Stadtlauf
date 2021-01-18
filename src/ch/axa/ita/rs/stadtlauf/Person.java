package ch.axa.ita.rs.stadtlauf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Person {
    private String number;
    private String category;
    private String name;
    private String begin;
    private String end;
    private int rang;

    public Person(String number, String category, String name, String end) {
        this.number = number;
        this.category = category;
        this.name = name;

        switch (category) {
            case "1":
                this.begin = "14:00:00";
                break;
            case "2":
                this.begin = "14:15:00";
                break;
            default:
                this.begin = "15:00:00";
        }

        this.end = end;
    }

    public String getNumber() {
        return number;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public String getDuration() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

            Date begin = format.parse(this.begin);
            Date end = format.parse(this.end);

            long result = end.getTime() - begin.getTime();
            long hours = TimeUnit.MILLISECONDS.toHours(result);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(result) - TimeUnit.HOURS.toMinutes(hours);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(result) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }
}
