# InovaGAB — versão final funcional

## Como abrir corretamente

Abra no Android Studio a pasta:

```text
InovaGAB_Funcional_Final/InovaGAB
```

A pasta correta é a que contém:

```text
settings.gradle
build.gradle
gradle.properties
app/
```

## Firebase

Antes de rodar, coloque seu arquivo:

```text
google-services.json
```

dentro de:

```text
InovaGAB/app/google-services.json
```

## Regras do Firestore

As regras estão em:

```text
firebase/firestore.rules
```

Cole essas regras no Firebase Console.

## Fluxo funcional

- Cadastro/login com Firebase Auth.
- Redirecionamento por perfil.
- Operador cria ideias e vê suas ideias.
- Gestor avalia ideias, aprova/rejeita e cria projetos.
- Líder vê dashboard e gerencia estratégias.
- Menu inferior clicável por perfil.
- Inputs corrigidos com EditText simples.
