# Credit Radar

Análise de crédito de fundos listados brasileiros, a partir dos documentos primários das
operações. O recorte atual é o **KNCR11**: em vez de repetir o que o relatório gerencial já diz,
o projeto vai ao termo de securitização de cada CRI para responder quem é a devedora, o que
garante o papel e se a cobertura está de pé.

A pergunta que orienta o produto é a do título do painel: **quem recebe o seu crédito?**

## O que o projeto tem hoje

- 19 posições de CRI importadas do relatório gerencial de julho de 2026 (um recorte, não a
  carteira inteira), com concentração por devedor, setor e grupo econômico.
- **Dossiê de crédito individual** para as 8 operações prioritárias, com nome jurídico, CNPJ,
  cedente, imóvel, garantias e fonte primária de cada campo.
- **Separação entre adimplência de pagamento e saúde da garantia** — a leitura central do
  projeto, explicada abaixo.

## Pagamento em dia não é garantia saudável

Todos os agentes fiduciários publicam um único status por operação, quase sempre "Adimplente".
Esse rótulo cobre apenas o pagamento de juros e amortização. Ele não diz nada sobre LTV, índice
de cobertura, formalização de garantia ou relatórios que o devedor deixou de entregar.

Por isso o painel usa dois selos independentes em cada operação:

| Selo | O que mede |
| --- | --- |
| **Pagamento** | Houve atraso em juros ou amortização |
| **Garantia** | Cobertura enquadrada, pendências abertas, waiver vigente ou ausência de informação |

O caso que justifica a separação é o **BRCR — Torre Almirante**. Ele aparece como adimplente e
com zero obrigações vencidas em aberto — o melhor cadastro da amostra. O relatório de garantias
conta outra história: LTV de 88,74% em janeiro de 2024 contra teto de 75%, índice de cobertura de
-2,11 em agosto de 2024 contra mínimo de 1,3, ambos dispensados por assembleia, e um mês em que o
fluxo de recursos foi insuficiente e o pagamento só saiu porque o devedor aportou dinheiro do
próprio bolso. Um selo só nunca mostraria isso.

## Regras de evidência

- Nenhuma identidade jurídica, CNPJ ou grupo econômico é atribuído sem fonte primária. Onde o
  termo não foi localizado em fonte aberta, o campo diz **"A confirmar no termo de securitização"**.
- Toda posição e todo dossiê guardam a URL e a data do documento de origem.
- Notificação de agente fiduciário é lida por categoria: dever de informação da emissora não é
  tratado como risco de crédito; razão e formalização de garantia, sim.

O caminho percorrido até cada documento — as APIs e portais de BariSec, Vórtx, Província,
Oliveira Trust e VERT — está registrado em [`docs/kncr11/leitura-dos-termos.md`](docs/kncr11/leitura-dos-termos.md),
junto com as correções que a leitura produziu na base.

## Executar localmente

```bash
docker compose up --build
```

Painel em `http://localhost:5173`, API em `http://localhost:8080/api`, health em
`/actuator/health`. No painel, clicar em qualquer ponto da linha na seção "O que já foi
confirmado" abre o dossiê da operação.

## Arquitetura

```text
React + TypeScript  ->  Spring Boot API  ->  PostgreSQL
                         |       |
                       Docker   AWS (ECS, RDS, ECR)
```

## API

- `GET /api/debtors`: dossiês de crédito, com `paymentStatus`, `collateralStatus` e `collateralNote`.
- `GET /api/research/cri-documents`: documentos localizados por operação e o que cada um provou.
- `GET /api/analytics/concentration?ticker=KNCR11`: exposição por devedor, setor e indexador.
- `GET /api/analytics/compare?ticker=KNCR11&from=2024-11-30&to=2026-07-31`: compara duas datas.
  A comparação só é marcada como pronta quando há posições detalhadas nas duas datas — uma
  fotografia agregada não é interpretada como saída de CRIs.
- `POST /api/documents` e `POST /api/positions`: importação manual de documento e posição.

## Próximos passos

1. Fechar as duas devedoras que seguem "A confirmar" (CPOF — Ed. FL Plaza e HSLG — BTS Meli),
   ambas da Riza e sem termo em área aberta.
2. Ler os relatórios de garantia das demais operações para medir cobertura, hoje disponível
   apenas para Torre Almirante.
3. Consolidar a nota do KNCR11: concentração por grupo, setor e tipo de garantia, e um score de
   transparência da carteira.
4. Só então repetir o processo para outros FIIs.

Em `infra/` há a base para executar a API em ECS Fargate com RDS PostgreSQL. Antes de aplicar,
preencha as variáveis sensíveis por um gerenciador de segredos e revise rede, domínio e custos.
