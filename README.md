# Sistema de Cadastro de Funcionários para Petshop

## Objetivo do Projeto
Este projeto foi desenvolvido como parte da UC de Programação de Soluções Computacionais, com o objetivo de consolidar os conceitos fundamentais de Programação Orientada a Objetos (POO). O sistema permite o cadastro e gerenciamento de usuários, projetos e equipes de um petshop.

## Funcionalidades
- Cadastro de usuários com informações como nome, CPF, e-mail, cargo, login e senha.
- Cadastro de projetos com nome, descrição, datas e status.
- Cadastro de equipes com nome, descrição e membros.
- Listagem de usuários, projetos e equipes.
- Persistência de dados em memória utilizando `ArrayList`.
- Tratamento de erros para entradas inválidas.
- Interface gráfica com JavaFX para todas as operações de CRUD.
- Validação de entrada para campos numéricos e de texto.
- Métodos `toString()` implementados para melhor exibição de dados no console.

## Tecnologias Utilizadas
- Linguagem: Java
- Estruturas de Dados: `ArrayList`
- Framework: JavaFX 24.0.2
- IDE: Visual Studio Code
- Controle de Versão: Git (GitHub)

```bash
javac src/*.java
java src.Main
```

## Estrutura do Projeto
- `Usuario.java`: Classe para representar os usuários do sistema.
- `Projeto.java`: Classe para representar os projetos do sistema.
- `Equipe.java`: Classe para representar as equipes do sistema.
- `Pessoa.java`: Classe base para herança.
- `Repositorio.java`: Classe para gerenciar a persistência de dados em memória.
- `Main.java`: Classe principal com o menu interativo.
- `FXML` e `Controladores`: Arquivos e classes para a interface gráfica.

## Autores
Desenvolvido como parte do curso de Análise e Desenvolvimento de Sistemas (ADS).
- Projeto Aplicado A3: Programação de soluções computacionais - DIGITAL
    - Turma 8439111 - PROGRAMAÇÃO DE SOLUÇÕES COMPUTACIONAIS (8C) - Digital 2025/2
        - Valdinei da Silva Ferreira
        - Ailton Kayky Cordeiro da Silva
        - Rodolfo Hernani Costa de Oliveira
        - Carlos Eduardo Oliveira da Silva