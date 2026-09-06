# DevFinance

Aplicação full stack de controle financeiro pessoal, pensada como peça de portfólio. Demonstra uma API REST em Java/Spring Boot, interface React/TypeScript, PostgreSQL, containers Docker e infraestrutura AWS declarada em Terraform.

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

- Cadastro de receitas e despesas
- Categorias e filtros por mês
- Resumo com saldo, receitas, despesas e gastos por categoria
- Dados de demonstração carregados na primeira execução

## Próximos passos para produção

Em `infra/` há a base para executar a API em ECS Fargate e usar RDS PostgreSQL. Antes de aplicar, preencha as variáveis sensíveis por um gerenciador de segredos e revise rede, domínio e custos.
