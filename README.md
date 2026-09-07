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
- **Score de transparência** por operação e da carteira, medindo o que é verificável em fonte
  aberta.
- **Coletores versionados** em [`collectors/`](collectors/README.md): o caminho até cada documento,
  executável, só com a biblioteca padrão do Python.
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

## Score de transparência

O segundo eixo do produto. Ao ler os oito termos, o padrão que apareceu não foi o risco de
crédito — foi a desigualdade de informação entre operações que dividem a mesma carteira. O score
mede quanto de cada operação é verificável em fonte aberta, em seis eixos: devedora, termo,
garantias, cobertura, demonstrações e acompanhamento.

**38% na carteira, ponderado pela exposição.** Quatro números explicam o resto:

- **R$ 392,3 mi (40% do recorte)** está em operações onde nem a devedora é identificável.
- **93% do recorte** não tem LTV nem índice de cobertura publicado.
- **Nenhuma das oito** tem demonstrações financeiras da devedora.
- **Nenhuma das oito** tem acompanhamento corrente: três não publicam nada e as outras cinco estão
  com o informe mais recente entre 6 e 8 meses atrás.

A ponderação piora o número em relação à média simples (40%) porque as duas operações mais
opacas são a segunda e a terceira maiores do recorte. Quanto maior o cheque, menos se sabe.

**Evidência vence.** Cobertura cai para parcial com 18 meses; acompanhamento, com 6. Foi ao datar
o segundo eixo que apareceu o problema: o `Informe_Mensal` que os agentes fiduciários publicam na
área aberta é, na prática, **um documento por ano, sempre referente a dezembro**. O nome diz
mensal, a regra é mensal, a publicação é anual — e entre março e dezembro quem consulta está
olhando dados de até doze meses atrás.

Detalhamento por eixo e limites do modelo em
[`docs/kncr11/score-de-transparencia.md`](docs/kncr11/score-de-transparencia.md).

## Cobertura calculada onde ninguém publica

No CRI HGBS/Bauru parte da garantia são cotas de dois FIIs listados, com quantidade definida no
termo e preço observável. Dá para medir o que o agente fiduciário não divulga: em 04/09/2026, a
parcela mensurável da garantia cobria **66,5%** do saldo das duas séries.

O número vem com a ressalva que importa: 61% dessa garantia está em FLRP11, e as cotas empenhadas
equivalem a **6,1 vezes todo o giro do papel em doze meses**. É marcação, não valor de saída.

O cálculo **não entra no score de transparência** — o score mede o que o mercado divulga, e
trabalho próprio não torna a operação mais transparente para quem não o fez. Método e ressalvas em
[`docs/kncr11/cobertura-calculada.md`](docs/kncr11/cobertura-calculada.md); código em
[`collectors/coverage_bauru.py`](collectors/coverage_bauru.py).

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
- `GET /api/analytics/transparency`: eixos, score por operação e agregados da carteira.
- `GET /api/analytics/coverage`: coberturas calculadas aqui, com data-base, método e ressalvas.
- `GET /api/research/cri-documents`: documentos localizados por operação e o que cada um provou.
- `GET /api/analytics/concentration?ticker=KNCR11`: exposição por devedor, setor e indexador.
- `GET /api/analytics/compare?ticker=KNCR11&from=2024-11-30&to=2026-07-31`: compara duas datas.
  A comparação só é marcada como pronta quando há posições detalhadas nas duas datas — uma
  fotografia agregada não é interpretada como saída de CRIs.
- `POST /api/documents` e `POST /api/positions`: importação manual de documento e posição.

## Próximos passos

1. Achar outra via para as duas devedoras que seguem "A confirmar" (CPOF — Ed. FL Plaza e HSLG —
   BTS Meli). O FNET/CVM está descartado: o filtro por CNPJ é ignorado pelo servidor e a busca por
   palavra-chave responde HTTP 500, então o repositório oficial não permite a consulta.
2. Ler os relatórios de garantia das demais operações para medir cobertura, hoje disponível
   apenas para Torre Almirante.
3. Medir frequência, não só idade: quem publicou uma vez em 12/2025 e quem publicou todo mês até
   12/2025 pontuam igual hoje, embora só o segundo esteja em regime de acompanhamento.
4. Só então repetir o processo para outros FIIs.

Em `infra/` há a base para executar a API em ECS Fargate com RDS PostgreSQL. Antes de aplicar,
preencha as variáveis sensíveis por um gerenciador de segredos e revise rede, domínio e custos.
