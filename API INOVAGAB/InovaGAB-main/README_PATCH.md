# Patch — Navegação inferior e inputs

## Como aplicar

Copie a pasta `app/` deste patch por cima da pasta `app/` do seu projeto atual.

No Windows:

1. Extraia este ZIP.
2. Copie a pasta `app`.
3. Cole dentro da pasta raiz `InovaGAB`.
4. Aceite substituir os arquivos.

Depois no Android Studio:

```text
Build > Clean Project
Build > Rebuild Project
```

## O que foi corrigido

- Menu inferior agora é clicável.
- Menus inferiores respeitam o perfil:
  - Operador: Home, Ideias, Perfil.
  - Gestor: Home, Ideias, Projetos, Perfil.
  - Líder: Home, Dashboard, Estratégias, Perfil.
- Adicionada tela `ProfileActivity`.
- Inputs foram trocados para `EditText` simples e estáveis.
- Removido uso problemático de `TextInputLayout` programático nos formulários.
