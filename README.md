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

## 🚀 Como executar o projeto

### Pré-requisitos
- Java 17+
- Maven
- MySQL ou outro banco relacional compatível com Hibernate

### Passos
1. Clone o repositório:
   ```bash
   git clone https://github.com/robinhokcond1/desafio-alphaSoftware.git
   ```
2. Configure o banco de dados no `hibernate.cfg.xml`
3. Execute o projeto no IntelliJ (ou outro IDE com suporte a Java Swing)
4. A tela principal será exibida com todas as funcionalidades

---

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

---

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
