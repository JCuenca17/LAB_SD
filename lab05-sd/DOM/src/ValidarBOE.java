import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ValidarBOE {
    public static void main(String[] args) {
        try {
            // Ruta del archivo XML
            File archivo = new File("boe.xml");

            // Parser DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //DOM
            factory.setNamespaceAware(true); // Reconocer espacios entre nombres
            factory.setValidating(false); // Solo bien formado

            DocumentBuilder builder = factory.newDocumentBuilder(); // Crea el parser
            builder.setErrorHandler(new org.xml.sax.helpers.DefaultHandler()); // Manejo de errores

            Document doc = builder.parse(archivo); // Carga el archivo XML

            System.out.println("El archivo boe.xml está BIEN FORMADO."); // Mensaje de éxito

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage()); // Mensaje de error
        }
    }
}
