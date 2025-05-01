# Desafio Java Backend

Sistema desenvolvido como parte de um desafio técnico para vaga de desenvolvedor backend Java. Trata-se de uma aplicação desktop para cadastro, gestão e consulta de livros, com persistência em banco de dados relacional e integração com a API OpenLibrary.

---

## 💡 Objetivo
Permitir o cadastro de livros com os dados:
- Título
- Autores
- ISBN
- Data de publicação
- Editora
- Livros semelhantes

---

## ✅ Pré-requisitos
- Java 17+
- Maven
- MySQL (caso deseje rodar fora do H2)

## 📦 Como executar

### Clonar o projeto
```bash
git clone git clone https://github.com/robinhokcond1/desafio-alphaSoftware.git
cd desafio-java-backend
```

### Compilar e executar
```bash
mvn clean compile exec:java -Dexec.mainClass="app.BibliotecaApp"
```

### Rodar testes
```bash
mvn test
```
## ✅ Funcionalidades implementadas

- [x] CRUD completo de livros
- [x] Interface Swing amigável e funcional
- [x] Pesquisa por campo (título, autores, etc.)
- [x] Importação de livros via CSV
- [x] Importação de livros via lista de ISBNs (OpenLibrary API)
- [x] Atualização automática de livros existentes por ISBN
- [x] Exportação para CSV
- [x] Exportação para XML
- [x] Feedback de progresso com diálogo de carregamento
- [x] Validação de ISBN e campos obrigatórios
- [x] Mensagens de erro claras para o usuário final
- [x] Cobertura de testes automatizados (DAO, validações)

---
> Durante os testes é usado o banco H2 em memória, configurado automaticamente via Hibernate.

## 📁 Estrutura do Projeto
```
├── model           # Entidades JPA (Livro)
├── dao             # Acesso a dados (LivroDAO)
├── util            # Serviços auxiliares (import/export, integração)
├── view            # Interfaces Swing
├── app             # Classe principal (BibliotecaApp)
├── test            # Testes unitários (JUnit + H2)
└── resources       # Arquivo de configuração Hibernate
```

## 📁 Exemplo de CSV para importação
```csv
titulo,autores,isbn,editora,dataPublicacao,livrosSemelhantes
Clean Code,Robert C. Martin,9780132350884,Prentice Hall,2008-08-01,Effective Java
Effective Java,Joshua Bloch,9780134685991,Addison-Wesley,2018-01-01,Clean Code
```

---

## 🛠 Tecnologias utilizadas
- Java 17
- Java Swing
- Hibernate (JPA)
- MySQL (pode ser substituído por PostgreSQL, H2, etc.)
- Jackson (para JSON)
- API OpenLibrary (https://openlibrary.org/developers/api)

---

## 🏆 Diferenciais desta entrega
- Atualização inteligente via ISBN durante importação
- Importação com validação + tratamento detalhado de erros
- Interface fluida com controle de estado (ex: diálogo de carregamento)
- Código limpo, separado em camadas e fácil de manter

---

## 📬 Contato
Robson Ramos  
Email: [robinhokcond1@hotmail.com](mailto:robinhokcond1@hotmail.com)  
LinkedIn: [https://www.linkedin.com/in/robson-luis-ramos-06760212b/](https://www.linkedin.com/in/robson-luis-ramos-06760212b/)
