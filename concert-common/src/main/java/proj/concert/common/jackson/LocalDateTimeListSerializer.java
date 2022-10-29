package proj.concert.common.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LocalDateTimeListSerializer extends StdSerializer<List<LocalDateTime>> {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public LocalDateTimeListSerializer() {
        this(null);
    }

    public LocalDateTimeListSerializer(Class<List<LocalDateTime>> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(List<LocalDateTime> localDates, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for(LocalDateTime date: localDates){
            jsonGenerator.writeString(date.format(FORMATTER));
        }
        jsonGenerator.writeEndArray();
    }
}
