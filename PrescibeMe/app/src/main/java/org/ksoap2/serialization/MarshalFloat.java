package org.ksoap2.serialization;

/**
 * Created by NAKEDIT CAMPUS on 2017-07-25.
 */

        import java.io.*;

        import org.xmlpull.v1.*;

public class MarshalFloat implements Marshal {

    public Object readInstance(XmlPullParser parser, String namespace, String name, PropertyInfo propertyInfo)
            throws IOException, XmlPullParserException {
        String stringValue = parser.nextText();
        Object result;
        if (name.equals("float")) {
            result = new Float(stringValue);
        } else if (name.equals("double")) {
            result = new Double(stringValue);
        } else if (name.equals("decimal")) {
            result = new java.math.BigDecimal(stringValue);
        } else {
            throw new RuntimeException("float, double, or decimal expected");
        }
        return result;
    }

    public void writeInstance(XmlSerializer writer, Object instance) throws IOException {
        writer.text(instance.toString());
    }

    public void register(SoapSerializationEnvelope cm) {
        cm.addMapping(cm.xsd, "float", Float.class, this);
        cm.addMapping(cm.xsd, "double", Double.class, this);
        cm.addMapping(cm.xsd, "decimal", java.math.BigDecimal.class, this);
    }
}