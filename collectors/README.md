# Coletores

O que produziu os dossiês e o score de transparência. Antes isso existia só como descrição em
prosa; aqui está como código executável, para que qualquer afirmação do painel possa ser
reconferida contra a fonte.

Só biblioteca padrão do Python 3.11+. Nada para instalar.

```bash
cd collectors
python3 coverage_bauru.py
```

## Por que cada portal exige um caminho diferente

Os sites de agente fiduciário e securitizadora são aplicações JavaScript ou WebForms: a página
renderizada não serve como fonte. Cada coletor vai à camada de dados por onde ela existe.

| Módulo | Fonte | Caminho |
| --- | --- | --- |
| `vortx.py` | Vórtx | API interna do site; a busca resolve código IF para id numérico |
| `bari.py` | BariSec | payload do Next.js embutido no HTML da emissão |
| `provincia.py` | Província | postback ASP.NET que libera o link do `FileHandler` |
| `oliveiratrust.py` | Oliveira Trust | API aberta da área do investidor, busca por código IF |
| `quotes.py` | B3, via série histórica | preço e volume de FIIs listados dados em garantia |
| `fnet.py` | Fundos.NET / B3 | demonstrações financeiras e laudos de FIIs por CNPJ |

O passo que mais rendeu foi a busca da Vórtx. Ela traduz o código IF para o id interno que todas
as outras rotas exigem, e foi assim que apareceram Torre Almirante e Eldorado — que este projeto
procurava nos portais errados.

## Cuidados que estão no código, não só aqui

**URLs assinadas expiram.** Os documentos da Vórtx vêm com assinatura de curta validade: listar e
baixar precisam acontecer na mesma execução.

**Nem toda pendência é risco de crédito.** `vortx.pending_obligations_by_category` agrupa as
obrigações vencidas por categoria em vez de somar. Relatório periódico e demonstração financeira
costumam ser dever da emissora; razão e formalização de garantia recaem sobre o devedor e sobre a
cobertura. Somar tudo apaga exatamente o que interessa.

**"Inadimplente" da Oliveira Trust não é falha de pagamento.** O status cobre descumprimento de
qualquer obrigação. No BTS Meli são pendências de relatório gerencial, relatório mensal, relatório
de obras e índice financeiro.

**Saldo por PU só vale para bullet.** `oliveiratrust.outstanding_balance` multiplica quantidade
por preço unitário. Em papel com amortização programada isso superestima o saldo; o campo
`amortizacao` da ficha diz qual é o caso.

**A fonte de cotação recusa o User-Agent completo do Chrome** quando ele vem sem os cabeçalhos
Sec-CH de um navegador real, e responde 429. Por isso `quotes.py` usa uma sessão própria com
User-Agent genérico.

## `coverage_bauru.py`

Calcula a cobertura do CRI HGBS/Bauru, que o agente fiduciário não publica. Cruza o saldo devedor
das duas séries com o valor de mercado das cotas de HPDP11 e FLRP11 dadas em garantia, cujas
quantidades vêm do termo de securitização.

Resultado em 04/09/2026: **164,0% de cobertura estimada** — R$ 158,7 mi de garantias contra
R$ 96,5 mi de saldo. O cálculo soma R$ 64,2 mi em cotas empenhadas e R$ 94,5 mi referentes a 35%
do Boulevard Shopping Bauru, com base em laudo Capright publicado pelo HGBS11 no FNET. Fundos de
reserva e despesas ficam fora. A participação minoritária foi tomada proporcionalmente e pode não
valer pro rata em uma execução.

O script também reporta liquidez, e é aí que o número perde parte do sentido: as 1.969.834 cotas
de FLRP11 empenhadas equivalem a **6,1 vezes todo o giro do papel em doze meses**. Preço de tela é
marcação, não valor de saída.

## Limites do FNET/CVM

O coletor `fnet.py` consulta documentos de FII e FIDC pelo campo `cnpjFundo`; por isso ele permite
localizar demonstrações financeiras e laudos quando a devedora é um fundo identificado. A busca
textual `palavraChave` continua retornando HTTP 500, e CRI não pode ser encontrado pelo CNPJ da
securitizadora nesse fluxo.

Por isso as devedoras de FL Plaza e BTS Meli seguem “A confirmar”: falta identificar a entidade
jurídica no termo ou em fonte primária, não apenas consultar o cadastro da securitizadora.
