package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class MainController {

    @FXML
    private void handleCadastrarUsuario() {
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CadastroUsuarioView.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Usuário");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCadastrarProjeto() {
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CadastroProjetoView.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Projeto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCadastrarEquipe() {
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CadastroEquipeView.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Equipe");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleListarUsuarios() {
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ListagemUsuariosView.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Listagem de Usuários");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleListarProjetos() {
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ListagemProjetosView.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Listagem de Projetos");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleListarEquipes() {
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ListagemEquipesView.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Listagem de Equipes");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSair() {
        System.exit(0);
    }
}