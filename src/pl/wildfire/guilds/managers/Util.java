package pl.wildfire.guilds.managers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String getDate(long time) {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date(time));
    }

}
