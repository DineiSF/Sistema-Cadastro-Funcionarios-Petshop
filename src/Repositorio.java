import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;

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

    public static Repositorio getInstance() {
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
    
    public boolean removerUsuario(Object usuario) {
        try {
            if (usuario != null) {
                Method getCpfMethod = usuario.getClass().getMethod("getCpf");
                String cpf = (String) getCpfMethod.invoke(usuario);
                return removerUsuario(cpf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
    
    public boolean removerProjeto(Object projeto) {
        try {
            if (projeto != null) {
                Method getNomeMethod = projeto.getClass().getMethod("getNome");
                String nome = (String) getNomeMethod.invoke(projeto);
                return removerProjeto(nome);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Métodos para gerenciar equipes
    public void adicionarEquipe(Equipe equipe) {
        equipes.add(equipe);
    }

    public List<Equipe> getEquipes() {
        return equipes;
    }

    public boolean removerEquipe(String nome) {
        // Verifica se a equipe existe antes de remover
        Equipe equipeParaRemover = null;
        for (Equipe equipe : equipes) {
            if (equipe.getNome().equals(nome)) {
                equipeParaRemover = equipe;
                break;
            }
        }
        
        if (equipeParaRemover != null) {
            // Limpa os membros da equipe
            equipeParaRemover.limparMembros();
            // Desassocia a equipe do projeto, se houver
            equipeParaRemover.setProjeto(null);
            // Remove a equipe da lista
            return equipes.remove(equipeParaRemover);
        }
        
        return false;
    }
    
    public boolean removerEquipe(Object equipe) {
        try {
            if (equipe != null) {
                Method getNomeMethod = equipe.getClass().getMethod("getNome");
                String nome = (String) getNomeMethod.invoke(equipe);
                return removerEquipe(nome);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
