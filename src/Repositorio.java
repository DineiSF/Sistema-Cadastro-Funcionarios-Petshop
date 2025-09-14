import java.util.ArrayList;
import java.util.List;

public class Repositorio {
    private static Repositorio instancia;

    private List<Usuario> usuarios;
    private List<Projeto> projetos;
    private List<Equipe> equipes;

    private Repositorio() {
        this.usuarios = new ArrayList<>();
        this.projetos = new ArrayList<>();
        this.equipes = new ArrayList<>();
    }

    public static Repositorio getInstancia() {
        if (instancia == null) {
            instancia = new Repositorio();
        }
        return instancia;
    }

    // Métodos para gerenciar usuários
    public void adicionarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public boolean removerUsuario(String cpf) {
        return usuarios.removeIf(usuario -> usuario.getCpf().equals(cpf));
    }

    // Métodos para gerenciar projetos
    public void adicionarProjeto(Projeto projeto) {
        projetos.add(projeto);
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public boolean removerProjeto(String nome) {
        return projetos.removeIf(projeto -> projeto.getNome().equals(nome));
    }

    // Métodos para gerenciar equipes
    public void adicionarEquipe(Equipe equipe) {
        equipes.add(equipe);
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public boolean removerEquipe(String nome) {
        return equipes.removeIf(equipe -> equipe.getNome().equals(nome));
    }
}
