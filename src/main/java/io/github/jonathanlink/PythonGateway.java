package io.github.jonathanlink;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import py4j.GatewayServer;


public class PythonGateway {
    
    private static final int GATEWAY_PORT = 25333;
    
    public Map<String, String> strip(String file){
        Map<String, String> retVal = new HashMap();
        PDDocument pdDocument = null;
        try {
            PDFParser pdfParser = new PDFParser(new RandomAccessFile(new File(file), "r"));
            pdfParser.parse();
            pdDocument = new PDDocument(pdfParser.getDocument());
            PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
            retVal.put("payload", pdfTextStripper.getText(pdDocument));
            retVal.put("success", "true");
        } catch (Exception e) {
            retVal.put("success", "false");
            retVal.put("error", e.getMessage());
        } finally {
            if(pdDocument != null) {
                try {
                    pdDocument.close();
                } catch (IOException ex) {
                }
            }
        }
        return retVal;
    }
    
    public static void main(String[] args) {
        int port = GATEWAY_PORT;
        if(args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        GatewayServer server = new GatewayServer(new PythonGateway(), port);
        server.start();
        System.out.println("PythonGateway is running on port " + port);
    }
}
