# KNCR11 — leitura dos termos de securitização

Registro de como cada informação do dossiê de crédito foi obtida. A regra do projeto é
não atribuir identidade jurídica, CNPJ ou grupo econômico sem fonte primária: onde o termo
não foi localizado em fonte aberta, o campo permanece "A confirmar no termo de securitização".

## Método

Os portais dos agentes fiduciários e das securitizadoras são aplicações JavaScript, então a
página renderizada não serve como fonte. O caminho que funcionou foi ir à camada de dados:

- **BariSec** — o HTML da página `/emissoes/<código IF>` já embute o payload do Next.js com os
  metadados da emissão e as URLs assinadas dos PDFs (termo, aditamentos, atas, anúncios).
- **Vórtx** — `apis.vortx.com.br/vxsite/api/operacao/{id}/resumo` devolve emissora, série, volume,
  indexador, vencimento, garantias cadastradas, status de inadimplemento e a lista de obrigações
  vencidas em aberto; `/documentos-por-tipo/2` devolve termo, contratos e notificações.
  As URLs dos documentos são assinadas e expiram, então download e extração precisam ser feitos
  na mesma sessão.

Os PDFs foram convertidos com `pdftotext -layout` e as definições lidas direto da cláusula 1.

## O que foi confirmado

### 1. "TRX — O Parque" (26E4046156) — o rótulo da carteira não bate com o termo

O termo da 74ª emissão da Bari Securitizadora é o **CRI Sírio-Libanês (TRX Multiativos)**, não
uma operação do empreendimento O Parque. A devedora econômica é a **Sociedade Beneficente de
Senhoras – Hospital Sírio-Libanês (CNPJ 61.590.410/0001-24)**, na condição de locatária de um
contrato de locação atípica de 19/12/2023, aditado em 23/05/2024 e 24/11/2025. O lastro são 80%
de cada parcela do aluguel. O cedente é o **TRX Real Estate FII (CNPJ 28.548.288/0001-52)**,
administrado pela BRL Trust.

Não é uma SPE opaca: é uma entidade beneficente conhecida. Isso muda a leitura de risco da
posição, hoje classificada como "Escritórios" na carteira.

Ponto de atenção nas garantias: sobre os mesmos imóveis (matrículas 290.388 a 290.405 do 15º RI
de São Paulo) já existe alienação fiduciária anterior em favor da Opea, constituída em 17/08/2023.
Há ainda alienação fiduciária do Imóvel Piraporinha (matrícula 65.047, Diadema/SP), cessão
fiduciária dos recebíveis, Fundo de Reserva e Fundo de Despesas.

Divergência a resolver: o portal da Bari informa vencimento em 09/05/2046 e o painel registra
05/09/2046. Provável inversão de dia e mês; confirmar no termo.

### 2. "Edifício O Parque" (23H1250138) — devedor é um FII, não uma SPE

Devedor: **OPQ Fundo de Investimento Imobiliário – FII (CNPJ 49.152.919/0001-90)**. Emissora:
Opea Securitizadora. Lastro: compra e venda a prazo de unidades autônomas e vagas do
empreendimento O Parque, fração ideal da matrícula 266.552 do 15º RI de São Paulo. Garantias:
alienação fiduciária do imóvel, cessão fiduciária de direitos creditórios, alienação fiduciária de
cotas, carta fiança ou Fundo de Reserva, e ESA. R$ 215,9 mi, CDI + 2,5%, bullet, vencimento em
24/03/2028.

### 3. "CPOF — Ed. FL Plaza" (25B3552756) — emissora identificada, devedora não

Emissora: **Riza Securitizadora** (antiga Virgo), 250ª emissão, 2ª série, R$ 150 mi, CDI + 2,0%,
vencimento em 22/09/2034. O agente fiduciário identifica a operação internamente como caso
"Capitania". Adimplente e **sem nenhuma obrigação vencida em aberto** — o melhor histórico entre
as quatro operações verificadas. O termo não está na área aberta da Vórtx, então devedora, CNPJ e
grupo econômico seguem a confirmar.

### 4. "JW Marriott Hotel" (25H3372118) — adimplente no rótulo, com 23 pendências em aberto

Emissora: Opea, 455ª emissão, série única, R$ 160 mi, CDI + 1,60%, vencimento em 27/08/2035.
O status formal é "Adimplente", mas o cadastro do agente fiduciário lista **23 obrigações vencidas
em aberto entre 15/09/2025 e 28/08/2026**, das quais **12 de razão de garantia e 4 de formalização
de garantia**. A Vórtx emitiu notificações de descumprimento mês a mês em todo o período.

Essa distinção é o ponto central da análise: adimplência de pagamento não é a mesma coisa que
cumprimento das obrigações de cobertura e de formalização de garantia. Vale separar os dois no
painel.

## Como ler as notificações do agente fiduciário

Nem toda notificação é sinal de risco de crédito. Boa parte delas cobra deveres da **emissora**
(envio de demonstrações financeiras do patrimônio separado, informe mensal à CVM). O que
importa para o crédito são as categorias que recaem sobre o **devedor** e sobre a cobertura:
"Razão de Garantia" e "Formalização de Garantia". Por isso as contagens acima estão quebradas
por categoria e não somadas em um número único.

## O que ainda falta

Quatro operações continuam sem termo localizado em fonte aberta:

| Operação | Código IF | Onde procurar |
| --- | --- | --- |
| Ed. Eldorado | 24E1127893 | Opea, 279ª emissão 1ª série — portal atrás de Cloudflare |
| HGBS — Bauru Shopping | 25L3678075 | Província Securitizadora, 138ª emissão 2ª série |
| BRCR — Torre Almirante | 21L0729901 | VERT, 64ª emissão 2ª série |
| HSLG — BTS Meli | 24H2231337 | Riza/Virgo 199ª emissão 2ª série, agente Oliveira Trust |

Também faltam, para as quatro já mapeadas, as demonstrações financeiras das devedoras e os
relatórios de garantia que sustentam a razão de cobertura.
