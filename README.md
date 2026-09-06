# Credit Radar

Aplicação full stack para acompanhar fundos listados de crédito. A primeira carteira reúne FIIs de papel, Fiagro e FI-Infra, permitindo investigar a exposição a CRIs, CRAs, debêntures e, quando necessário, cotas de fundos de crédito.

## Arquitetura

```text
React + TypeScript  ->  Spring Boot API  ->  PostgreSQL
                         |       |
                       Docker   AWS (ECS, RDS, ECR)
```

## Executar localmente

```bash
docker compose up --build
```

Abra `http://localhost:5173`. A API fica em `http://localhost:8080/api` e o Actuator em `/actuator/health`.

## Funcionalidades

- Carteira inicial: KNCR11, MXRF11, CPTS11, IRIM11, RURA11, BISE11 e ISNN11
- Modelo unificado para CRI, CRA, debênture e cota de fundo
- API para listar ativos e posições de crédito
- Base preparada para importar informes da CVM e relatórios gerenciais

## Próximos passos para produção

Em `infra/` há a base para executar a API em ECS Fargate e usar RDS PostgreSQL. Antes de aplicar, preencha as variáveis sensíveis por um gerenciador de segredos e revise rede, domínio e custos.
