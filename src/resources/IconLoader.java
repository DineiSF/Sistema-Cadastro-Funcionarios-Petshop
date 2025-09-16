package resources;

import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class IconLoader {
    
    /**
     * Carrega o ícone da aplicação a partir de uma string Base64.
     * 
     * @return Imagem decodificada
     */
    public static Image loadAppIcon() {
        try {
            byte[] imageData = Base64.getDecoder().decode(AppIconBase64.ICON_BASE64);
            return new Image(new ByteArrayInputStream(imageData));
        } catch (Exception e) {
            System.err.println("Erro ao carregar ícone: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}