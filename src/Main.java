import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Repositorio repositorio = Repositorio.getInstancia();
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        do { 
            try{
                System.out.println("\n=== Menu Principal ===");
                System.out.println("1. Cadastrar Usuário");
                System.out.println("2. Cadastrar Projeto");
                System.out.println("3. Cadastrar Equipe");
                System.out.println("4. Listar Usuários");
                System.out.println("5. Listar Projetos");
                System.out.println("6. Listar Equipes");
                System.out.println("7. Remover Usuário");
                System.out.println("8. Remover Projeto");
                System.out.println("9. Remover Equipe");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha
                
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.next(); // Limpa a entrada inválida
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            }
        } while (opcao != 0);
        
    }
}
