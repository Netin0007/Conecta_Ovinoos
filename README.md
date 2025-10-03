# 🐑 Conecta:Ovinos

**Um aplicativo Android para fortalecer a agricultura familiar e conectar pequenos produtores ao mercado regional.**

![Status](https://img.shields.io/badge/status-MVP%20em%20Desenvolvimento-yellow)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blueviolet)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6-blue)
![SDK](https://img.shields.io/badge/Min%20SDK-26-brightgreen)

---

## 🎯 Sobre o Projeto

**Conecta:Ovinos** é uma ferramenta de gestão e marketplace pensada para o pequeno produtor rural. O objetivo é substituir o "caderno de anotações" por uma solução digital simples, que não apenas organiza o inventário (animais e produtos derivados), mas também ajuda o produtor a entender seus custos, lucros e a vender sua produção de forma direta e descomplicada.

A plataforma visa capacitar o produtor a enxergar seu trabalho não apenas como subsistência, mas como um negócio e um investimento.

---

## ✨ Funcionalidades do MVP Atual

A versão atual do projeto (MVP) já possui a interface e o fluxo de navegação completos para a **jornada crítica do produtor**:

-   ✅ **Gestão de Inventário:** Visualização de uma lista mista de produtos, incluindo animais (ovinos, caprinos) e produtos derivados (queijo, leite, mel, etc.).
-   ✅ **Cadastro de Produtos:** Um formulário inteligente que se adapta para cadastrar diferentes tipos de produtos, com campos específicos para cada um (ex: Raça para animais, Unidade de Medida para derivados).
-   ✅ **Controle de Custos:** O formulário de cadastro já inclui um campo para o "Custo de Produção", o primeiro passo para o controle financeiro.
-   ✅ **Fluxo de Venda:**
    -   Visualização de detalhes de um animal.
    -   Formulário para criar um anúncio de venda com preço e descrição.
-   ✅ **Visualização de Anúncios:** Tela para listar os anúncios já criados pelo produtor.
-   🎨 **Design System e Acessibilidade:**
    -   Interface moderna baseada no Material Design 3.
    -   Paleta de cores ("Pet Natura") com alto contraste, pensada para boa visibilidade em diferentes condições de iluminação.
    -   Uso de fontes escaláveis (`sp`) e alvos de toque grandes para garantir a acessibilidade.

---

## 🛠️ Tecnologias Utilizadas

Este projeto foi construído com as tecnologias mais modernas recomendadas pelo Google para o desenvolvimento Android:

-   **Linguagem:** [Kotlin](https://kotlinlang.org/)
-   **Interface Gráfica:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
-   **Arquitetura:** Baseada em MVVM (Model-View-ViewModel)
-   **Navegação:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
-   **Design:** [Material Design 3](https://m3.material.io/)

---

## 🚀 Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/Netin0007/Conecta_Ovinos.git](https://github.com/Netin0007/Conecta_Ovinos.git)
    ```
2.  **Abra no Android Studio:**
    -   Abra o Android Studio (versão Hedgehog ou mais recente recomendada).
    -   Selecione "Open an Existing Project" e aponte para a pasta clonada.
3.  **Sincronize o Gradle:**
    -   O Android Studio deve sincronizar o projeto automaticamente. Se não, clique no ícone de "Sync Project with Gradle Files" (elefante com seta).
4.  **Execute o App:**
    -   Selecione um emulador ou conecte um dispositivo físico.
    -   Clique no botão "Run" (▶️).

---

## 🔮 Próximos Passos (Handover)

O projeto está com a interface e a lógica de navegação prontas. O próximo grande passo é substituir os dados de exemplo (`dummy data`) por uma solução de backend real. A integração planejada é com o **Firebase**.

-   **1. Configurar o Firebase:** Conectar este projeto Android a um novo projeto no console do Firebase.
-   **2. Firebase Authentication:** Substituir a tela de login simulada por um sistema de cadastro/login real (ex: Email e Senha ou Telefone).
-   **3. Cloud Firestore:** Utilizar o Firestore como banco de dados NoSQL para:
    -   Salvar os produtos cadastrados por cada usuário.
    -   Salvar os anúncios criados.
    -   Ler os dados em tempo real para popular as telas de "Inventário" e "Meus Anúncios".
    -   Aproveitar o suporte offline do Firestore, que é crucial para o público-alvo rural.

---
