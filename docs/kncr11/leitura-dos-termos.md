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

- **Província Securitizadora** — o portal é ASP.NET WebForms. A grade de emissões é HTML servido
  inteiro; o detalhe fica em `Detalheemissao?id=<id interno>`, e o termo sai de um postback com
  `ddlTipoanexo` no `FileHandler.ashx`.
- **Oliveira Trust** — `services-ft.oliveiratrust.com.br/app/v1/titulos?busca=<código IF>` devolve a
  ficha completa do papel, incluindo garantias descritas por extenso, status de adimplência e as
  pendências que motivaram esse status.
- **VERT** — o portal de dados exige autenticação. O caminho aberto foi o Relatório Anual do Agente
  Fiduciário, e a própria VERT tem a Vórtx como agente fiduciário, então a operação também está na
  API pública da Vórtx.

A busca da Vórtx (`/investidor/dcm?busca=<termo ou código IF>`) resolve o código IF para o id
numérico da operação, que é o que as demais rotas da API pedem. Foi assim que apareceram Torre
Almirante e Eldorado, que estavam catalogados aqui sob outros agentes.

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


### 5. "Ed. Eldorado" (24E1127893) — não existe devedora única

O termo da 279ª emissão da Opea define como Devedor **os locatários dos Contratos de Locação** do
Eldorado Business Tower. Não há SPE nem devedora corporativa: o risco é pulverizado entre os
inquilinos. O cedente é o **EBT FII (CNPJ 21.085.863/0001-89)**. O imóvel é a fração ideal de 51%
de unidades autônomas do Eldorado Business Tower, Centro Empresarial Rebouças, Av. das Nações
Unidas 8.501 (matrículas 118.281 a 118.313, 10º RI de São Paulo).

A relação dos locatários está no Anexo IX, que não consta da versão pública do termo — por isso o
CNPJ segue "A confirmar". R$ 101,45 mi, CDI + 1,85%, bullet, vencimento em 15/03/2033. Adimplente,
com 11 obrigações vencidas em aberto entre 31/03/2025 e 28/08/2026.

Ponto de atenção: as garantias são **compartilhadas com os CRI Eldorado Anterior** (193ª e 204ª
séries da 1ª emissão da mesma emissora), e existem ainda os CRI Eldorado Subordinada (194ª e 205ª).
A cobertura desta série não é exclusiva dela.

O agente fiduciário é a Vórtx, não a Guide: a fonte antiga era uma página genérica de ofertas.

### 6. "HGBS — Bauru Shopping" (25L3678075) — devedora é um FII listado

Devedora: **Hedge Brasil Shopping FII, o HGBS11 (CNPJ 08.431.747/0001-06)**, administrado pela Hedge
Investments DTVM, que também é coordenadora líder da oferta. Emissora: Companhia Província de
Securitização. Cedente: **Pátria Malls FII (CNPJ 26.499.833/0001-32)**. O lastro é o preço de
aquisição devido pelo HGBS11 pela fração do Shopping Bauru.

Série 2 de duas: R$ 65,086 mi dos R$ 91,455 mi da emissão, CDI + 1,60%, vencimento em 20/12/2028,
sem agência de rating.

Ponto de atenção nas garantias: além da alienação fiduciária de 35% do Shopping Bauru (titularidade
da Vértico Bauru), boa parte da garantia são **cotas de outros FIIs do mesmo gestor** — 282.051
cotas do HPDP11 e 1.969.834 do FLRP11, ambas de titularidade da própria devedora. Garantia e
devedora ficam correlacionadas.

### 7. "BRCR — Torre Almirante" (21L0729901) — série errada na base, e a fonte apontava para outro papel

O código é da 64ª emissão **1ª série**, não 2ª. A 2ª série é outra operação: 21L0730156, R$ 150 mi,
IPCA + 7,10%, vencimento em 2036. Os números que já estavam aqui (R$ 95 mi, CDI + 2,60%, vencimento
em 20/12/2033) são os da 1ª série, então só o rótulo da série estava errado.

Devedor: **FII Prime Portfólio (CNPJ 10.427.246/0001-06)**, administrado pelo BTG Pactual Serviços
Financeiros. Cedente: **TA FII (CNPJ 21.085.472/0001-64)**. Imóvel: fração ideal de 60% da Torre
Almirante (Av. Almirante Barroso 81) e do Edifício Garagem (Av. Graça Aranha 327), no Rio de
Janeiro, matrículas 52.221 a 52.256 do 7º Ofício de RI. Garantias: alienação fiduciária de cotas,
cessão fiduciária de direitos creditórios, Fundo de Reserva e Fundo de Despesas.

**Adimplente e com zero obrigações vencidas em aberto**, confirmado no cadastro da Vórtx e no
Relatório Anual do Agente Fiduciário 2025 da VERT.

A fonte que estava registrada aqui (`RA_2023_CRA_SAO_MARTINHO_II...`) é o relatório de um CRA da
São Martinho, sem nenhuma relação com esta operação. Foi substituída.

### 8. "HSLG — BTS Meli" (24H2231337) — a Oliveira Trust classifica como INADIMPLENTE

A ficha pública da Oliveira Trust marca o status da operação como **INADIMPLENTE**, listando como
pendências relatório gerencial, relatório mensal, **relatório de obras** e **índice financeiro**.
Nenhuma delas é falha de pagamento, mas a exigência de relatório de obras sugere empreendimento
ainda em execução, e o índice financeiro em aberto impede aferir a cobertura.

Volume corrigido: a série é de **R$ 55 mi**, não R$ 40 mi como constava aqui; a emissão total, em
três séries, soma R$ 165 mi. CDI + 2,25%, emissão em 28/08/2024, vencimento em 28/04/2034.

Garantias: alienação fiduciária dos imóveis Araucária e Arujá; cessão fiduciária dos aluguéis do
Contrato de Locação Araucária, na proporção da fração ideal de 56,18% do Empreendimento Araucária
detida pelo fiduciante e sujeita ao Evento de Integração Araucária; promessa de cessão dos aluguéis
do Empreendimento Arujá para manter o Índice Mínimo de Cobertura; Fundo de Reserva; seguro
patrimonial e de lucros cessantes.

A devedora segue "A confirmar": o termo não está em área aberta e a ficha não a nomeia.

## Relatório de garantias: o caso Torre Almirante

A Vórtx publica, para esta operação, laudo de LTV e relatório mensal de acompanhamento — os
únicos documentos de cobertura abertos entre as oito. O que eles mostram contradiz o cadastro.

**Verificação de LTV, 15/01/2024**

| | |
| --- | --- |
| Saldo devedor total (1ª + 2ª séries) | R$ 303.887.976,21 |
| Avaliação do imóvel (laudo de 30/11/2023) | R$ 342.447.000,00 |
| LTV | **88,74%** |

Resultado acima de 75% é evento de vencimento antecipado não automático. A AGT de 18/12/2023
deliberou waiver para a verificação de 2024.

**Índice de cobertura, agosto de 2024**

| | |
| --- | --- |
| NOI de caixa (jun a ago/24) | **-R$ 3.208.155,10** |
| Somatório das parcelas CRI (ago/24) | R$ 1.520.648,14 |
| Índice de cobertura | **-2,11** |

Resultado inferior a 1,3 é evento de vencimento antecipado não automático. A mesma AGT dispensou
a observância entre dezembro de 2023 e julho de 2025, "a partir de quando o índice deverá voltar
a ser cumprido". Não há apuração pública posterior ao fim do waiver.

E em 12/08/2024 o relatório classifica o fluxo de recursos do mês como **insuficiente**: entraram
R$ 1.208.192,30 contra R$ 1.520.648,14 devidos. O pagamento saiu porque o FII Prime Portfólio
aportou R$ 525.849,70 em 16/08/2024. A operação estava pagando com dinheiro do devedor, não com
caixa do lastro.

Os fundos, esses, estavam enquadrados: reserva de R$ 1.538.577,18 contra mínimo de R$ 1.520.648,14
e despesas de R$ 79.217,19 contra mínimo de R$ 50.000,00.

### Por que isso mudou o painel

Torre Almirante é a operação com o melhor cadastro da amostra — adimplente, zero obrigações
vencidas — e a pior cobertura. Um status único nunca mostraria as duas coisas. Por isso cada
operação passou a ter dois selos independentes: **adimplência de pagamento** e **saúde da
garantia**, esta última em quatro estados — sem pendência, pendências abertas, sob waiver e sem
informação.

O último estado é tão informativo quanto os outros: duas operações não têm relatório de garantias
público nenhum, e nesses casos a ausência de alerta não significa cobertura verificada.

## O que ainda falta

Duas devedoras continuam sem identificação, por ausência de termo em fonte aberta:

| Operação | Código IF | O que falta |
| --- | --- | --- |
| CPOF — Ed. FL Plaza | 25B3552756 | Termo da 250ª emissão da Riza; a Vórtx só publica o resumo |
| HSLG — BTS Meli | 24H2231337 | Termo da 199ª emissão da Riza; a Oliveira Trust só publica a ficha |

O Ed. Eldorado tem devedora identificada por natureza — são os locatários — mas a lista nominal
depende do Anexo IX, ausente da versão pública do termo.

Faltam ainda, para todas as oito, as demonstrações financeiras das devedoras e os relatórios de
garantia que sustentam a razão de cobertura. Para Torre Almirante esses documentos existem na área
aberta da Vórtx (laudo, organograma, DFP e relatório de garantias) e são o próximo alvo natural.

## Correções que a leitura produziu na base

| Operação | O que estava registrado | O que o documento diz |
| --- | --- | --- |
| TRX — O Parque | operação de escritórios "O Parque" | CRI Sírio-Libanês, locação atípica hospitalar |
| BRCR — Torre Almirante | 64ª emissão, 2ª série | 64ª emissão, 1ª série |
| BRCR — Torre Almirante | fonte = relatório de um CRA da São Martinho | termo da 64ª emissão + RA 2025 da VERT |
| Ed. Eldorado | fonte = página de ofertas da Guide | termo da 279ª emissão na Vórtx |
| HSLG — BTS Meli | R$ 40,0 mi emitidos | R$ 55,0 mi na série, R$ 165 mi na emissão |
