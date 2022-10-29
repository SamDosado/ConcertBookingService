package proj.concert.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalDateTimeListDeserializer extends StdDeserializer<List<LocalDateTime>> {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public LocalDateTimeListDeserializer() {
        this(null);
    }

    public LocalDateTimeListDeserializer(Class<List<LocalDateTime>> clazz) {
        super(clazz);
    }

    @Override
    public List<LocalDateTime> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        List<LocalDateTime> dates = new ArrayList<LocalDateTime>();
        ArrayList<String> strList = new ArrayList<>();
        Arrays.asList(jsonParser.getText().split(" "));
        for(String str : strList){
            dates.add(LocalDateTime.parse(str, FORMATTER));
            System.out.println(dates);
        }
//        LocalDateTime.parse(jsonParser.getText(), FORMATTER);
        return  dates;
    }
}
