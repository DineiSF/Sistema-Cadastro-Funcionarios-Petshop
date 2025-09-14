import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Repositorio repositorio = Repositorio.getInstancia();
        Scanner scanner = new Scanner(System.in);
        int opcao = -1; // Inicializar com valor padrão

        do {
            try {
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

                switch (opcao) {
                    case 1:
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("CPF: ");
                        String cpf = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Cargo: ");
                        String cargo = scanner.nextLine();
                        System.out.print("Login: ");
                        String login = scanner.nextLine();
                        System.out.print("Senha: ");
                        String senha = scanner.nextLine();
                        repositorio.adicionarUsuario(new Usuario(nome, cpf, email, cargo, login, senha));
                        System.out.println("Usuário cadastrado com sucesso!");
                        break;
                    case 2:
                        System.out.print("Nome do Projeto: ");
                        String nomeProjeto = scanner.nextLine();
                        System.out.print("Descrição: ");
                        String descricao = scanner.nextLine();
                        System.out.print("Data de Início (yyyy-mm-dd): ");
                        String dataInicio = scanner.nextLine();
                        System.out.print("Data de Término Prevista (yyyy-mm-dd): ");
                        String dataTermino = scanner.nextLine();
                        System.out.print("Status: ");
                        String status = scanner.nextLine();
                        repositorio.adicionarProjeto(new Projeto(nomeProjeto, descricao, java.time.LocalDate.parse(dataInicio), java.time.LocalDate.parse(dataTermino), status));
                        System.out.println("Projeto cadastrado com sucesso!");
                        break;
                    case 3:
                        System.out.print("Nome da Equipe: ");
                        String nomeEquipe = scanner.nextLine();
                        System.out.print("Descrição: ");
                        String descricaoEquipe = scanner.nextLine();
                        repositorio.adicionarEquipe(new Equipe(nomeEquipe, descricaoEquipe));
                        System.out.println("Equipe cadastrada com sucesso!");
                        break;
                    case 4:
                        System.out.println("\n=== Lista de Usuários ===");
                        if (repositorio.getUsuarios().isEmpty()) {
                            System.out.println("Esta lista não contém dados.");
                        } else {
                            for (Usuario usuario : repositorio.getUsuarios()) {
                                System.out.println(usuario);
                            }
                        }
                        break;
                    case 5:
                        System.out.println("\n=== Lista de Projetos ===");
                        if (repositorio.getProjetos().isEmpty()) {
                            System.out.println("Esta lista não contém dados.");
                        } else {
                            for (Projeto projeto : repositorio.getProjetos()) {
                                System.out.println(projeto);
                            }
                        }
                        break;
                    case 6:
                        System.out.println("\n=== Lista de Equipes ===");
                        if (repositorio.getEquipes().isEmpty()) {
                            System.out.println("Esta lista não contém dados.");
                        } else {
                            for (Equipe equipe : repositorio.getEquipes()) {
                                System.out.println(equipe);
                            }
                        }
                        break;
                    case 7:
                        System.out.print("Digite o CPF do usuário a ser removido: ");
                        String cpfRemover = scanner.nextLine();
                        if (repositorio.removerUsuario(cpfRemover)) {
                            System.out.println("Usuário removido com sucesso!");
                        } else {
                            System.out.println("Usuário não encontrado.");
                        }
                        break;
                    case 8:
                        System.out.print("Digite o nome do projeto a ser removido: ");
                        String nomeProjetoRemover = scanner.nextLine();
                        if (repositorio.removerProjeto(nomeProjetoRemover)) {
                            System.out.println("Projeto removido com sucesso!");
                        } else {
                            System.out.println("Projeto não encontrado.");
                        }
                        break;
                    case 9:
                        System.out.print("Digite o nome da equipe a ser removida: ");
                        String nomeEquipeRemover = scanner.nextLine();
                        if (repositorio.removerEquipe(nomeEquipeRemover)) {
                            System.out.println("Equipe removida com sucesso!");
                        } else {
                            System.out.println("Equipe não encontrada.");
                        }
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida. Por favor, insira um número.");
                scanner.nextLine(); // Limpar entrada inválida
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        } while (opcao != 0);

        scanner.close();
    }
}
