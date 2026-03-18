# 🐑 Conecta:Ovinos

**Plataforma completa de gestão rural e marketplace para o fortalecimento da ovinocultura familiar.**

![Status](https://img.shields.io/badge/status-MVP%20Funcional-green)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blueviolet)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6-blue)
![Room](https://img.shields.io/badge/Database-Room-orange)

---

## 🎯 Sobre o Projeto

O **Conecta:Ovinos** é um ecossistema digital desenvolvido para transformar a realidade do pequeno produtor rural. Ele resolve dois problemas centrais: a falta de controle financeiro/produtivo e a dificuldade de acesso direto ao consumidor final.

O aplicativo funciona em dois modos principais:
1.  **Modo Produtor:** Uma ferramenta de gestão técnica e financeira (ERP Rural).
2.  **Modo Comprador (Feira):** Um marketplace para consumidores encontrarem produtos frescos e animais diretamente da origem.

---

## 🏗️ Arquitetura e Estrutura

O projeto segue os princípios da **Clean Architecture** e o padrão **MVVM (Model-View-ViewModel)**, garantindo separação de responsabilidades e facilidade de manutenção.

### 📂 Organização de Pastas (`app/src/main/java/com/example/conectaovinos/`)

-   **`/database`**: Camada de persistência local.
    -   **`/entities`**: Definição das tabelas (Animal, Produto, Anúncio, Transação).
    -   **`/dao`**: Interfaces de acesso aos dados (Queries SQL encapsuladas).
    -   `AppDatabase.kt`: Configuração central do Room com suporte a migrações.
-   **`/viewmodel`**: Camada de lógica de negócio. Gerencia o estado da UI e a comunicação com o banco de dados usando Coroutines e Flow.
-   **`/ui/theme`**: Identidade visual do projeto (Cores "Terra Barro", "Sol Nordeste", etc).
-   **Raiz do Projeto**: Contém as **Screens** (telas em Jetpack Compose) e a lógica de navegação central (`MainActivity.kt`).

---

## ⚙️ Como o Sistema Funciona

### 1. Persistência de Dados (Offline-First)
Diferente de apps que dependem 100% de internet, o Conecta:Ovinos utiliza o **Room Database**. Isso permite que o produtor cadastre animais e transações mesmo sem sinal no campo. Os dados são salvos localmente e carregados instantaneamente.

### 2. Gestão Financeira Inteligente
O sistema financeiro não é apenas uma lista de gastos. Ele cruza dados:
-   Calcula o **Valor do Rebanho** baseado no custo de aquisição de cada animal vivo.
-   Gera um **Saldo Geral** automático subtraindo despesas (ração, vacinas) e custos fixos das receitas (vendas de produtos).
-   Oferece um **Histórico de Movimentações** para auditoria rápida do produtor.

### 3. Ecossistema de Vendas (Marketplace)
O fluxo de venda é integrado:
1.  O produtor seleciona um animal do seu inventário.
2.  Cria um anúncio (o sistema sugere margem de lucro).
3.  O item aparece automaticamente na **Feira Livre** (Área do Comprador).
4.  Compradores podem favoritar itens, que ficam salvos em uma aba exclusiva via banco de dados.

---

## 🚀 Tecnologias e Bibliotecas

-   **Jetpack Compose:** UI declarativa e moderna.
-   **Room Persistence:** Banco de dados SQL local robusto.
-   **Kotlin Coroutines & Flow:** Processamento assíncrono para garantir que a interface nunca trave.
-   **Coil:** Carregamento eficiente de imagens e fotos dos animais.
-   **Navigation Compose:** Sistema de rotas tipo Single-Activity.

---

## 🛠️ Guia para Desenvolvedores (Handover)

Se você está assumindo este código agora, aqui estão pontos importantes:

-   **Navegação:** Centralizada no `MainActivity.kt`. Se criar uma nova tela, lembre-se de registrar a rota no `NavHost`.
-   **Banco de Dados:** Ao alterar qualquer `Entity`, você **deve** incrementar a `version` no `AppDatabase.kt`.
-   **Injeção de Dependência:** Atualmente usamos `ViewModelProvider.Factory` manuais para passar os DAOs para as ViewModels.
-   **Imagens:** O app suporta `fotoUri`. Certifique-se de tratar permissões de galeria ao implementar o seletor de fotos no futuro.

---

## 🎨 Identidade Visual
O projeto utiliza uma paleta de cores personalizada que remete ao semiárido brasileiro:
-   **TerraBarro (#9E3B2C):** Cor principal da marca.
-   **VerdeCaatinga (#2E5A39):** Representa saúde e lucro.
-   **SolNordeste (#FFB347):** Destaques e alertas importantes.

---
*Desenvolvido para transformar o suor do produtor em sucesso garantido.* 🌵🐑
