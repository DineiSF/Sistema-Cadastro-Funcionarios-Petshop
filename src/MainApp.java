import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import resources.IconLoader;
import java.io.File;
import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Tentar vários métodos para encontrar o arquivo FXML
        URL fxmlUrl = null;
        
        // Método 1: Tenta carregar como recurso no classpath
        fxmlUrl = getClass().getResource("/ui/MainView.fxml");
        
        // Método 2: Se não encontrar, tenta carregar do sistema de arquivos
        if (fxmlUrl == null) {
            File file = new File("bin/ui/MainView.fxml");
            if (file.exists()) {
                fxmlUrl = file.toURI().toURL();
                System.out.println("Arquivo FXML encontrado em: " + file.getAbsolutePath());
            }
        }
        
        // Se ainda não encontrou, tenta mais um caminho
        if (fxmlUrl == null) {
            File file = new File("src/ui/MainView.fxml");
            if (file.exists()) {
                fxmlUrl = file.toURI().toURL();
                System.out.println("Arquivo FXML encontrado em: " + file.getAbsolutePath());
            }
        }
        
        if (fxmlUrl == null) {
            throw new RuntimeException("Não foi possível encontrar o arquivo FXML");
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Sistema de Cadastro - Petshop");
        
        // Define o ícone da aplicação
        Image appIcon = IconLoader.loadAppIcon();
        if (appIcon != null) {
            primaryStage.getIcons().add(appIcon);
        }
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
