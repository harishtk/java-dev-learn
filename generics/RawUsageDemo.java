package generics;

import java.util.HashMap;
import java.util.Map;

public class RawUsageDemo {
    public static void main(String[] args) {
        unCorrectMethod();
        correctedMethod();
    }

    static void unCorrectMethod() {
        byte[] binaryData = new byte[0]; // Dummy binary data for demonstration
        Map successCode = new HashMap<>();
        Map<?, ?> payloadData = parseSignalValuesMap(new String());
        if(payloadData != null) {
            successCode.putAll(payloadData);
            if(binaryData != null) {
                Map resWithBinary = new HashMap<>();
                if(successCode.keySet().size() != 0){
                    Object firstKey = successCode.get(successCode.keySet().toArray()[0]);
                    resWithBinary.put("value", firstKey);
                    resWithBinary.put("binary", binaryData);
                    successCode.put(String.valueOf(firstKey), resWithBinary);
                }
            }
        }
        successCode.put("Success", "success");
        
        System.out.println(successCode);
    }

    static void correctedMethod() {
        byte[] binaryData = new byte[0]; // Dummy binary data for demonstration
        Map<String, Object> successCode = new HashMap<>();
        Map<?, ?> payloadData = parseSignalValuesMap(new String());
        if(payloadData != null) {
            for (Map.Entry<?, ?> entry : payloadData.entrySet()) {
                if(entry.getValue() instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) entry.getValue();
                    for (Map.Entry<?, ?> innerEntry : map.entrySet()) {
                        successCode.put(innerEntry.getKey().toString(), innerEntry.getValue());
                    }
                } else {
                    successCode.put(entry.getKey().toString(), entry.getValue());
                }
            }
            
            // This line won't work
            // successCode.putAll(payloadData);
            if(binaryData != null) {
                Map<String, Object> resWithBinary = new HashMap<>();
                if(successCode.keySet().size() != 0){
                    Object firstKey = successCode.get(successCode.keySet().toArray()[0]);
                    resWithBinary.put("value", firstKey);
                    resWithBinary.put("binary", binaryData);
                    successCode.put(String.valueOf(firstKey), resWithBinary);
                }
            }
        }
        successCode.put("Success", "success");
        
        System.out.println(successCode);
    }

    static Map<?, ?> parseSignalValuesMap(String signalValues) {
        // Dummy implementation for demonstration purposes
        return new HashMap<>();
    }
}
