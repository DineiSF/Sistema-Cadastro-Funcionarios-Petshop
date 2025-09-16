import java.util.ArrayList;
import java.util.List;

public class Equipe {
    private String nome;
    private String descricao;
    private List<Usuario> membros;
    private Projeto projeto;

    // Construtor parametrizado
    public Equipe(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.membros = new ArrayList<>();
        this.projeto = null;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void adicionarMembro(Usuario usuario) {
        this.membros.add(usuario);
    }

    public void removerMembro(Usuario usuario) {
        this.membros.remove(usuario);
    }
    
    public Projeto getProjeto() {
        return projeto;
    }
    
    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }
    
    public void limparMembros() {
        this.membros.clear();
    }
    
    @Override
    public String toString() {
        return "Equipe: " + nome + ", Descrição: " + descricao + ", Projeto Associado: " +
               (projeto != null ? projeto.getNome() : "Nenhum");
    }
}
