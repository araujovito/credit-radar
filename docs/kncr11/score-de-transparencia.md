# Score de transparência — KNCR11

O score não mede a qualidade do crédito. Mede **quanto de uma operação é verificável em fonte
aberta**: quem deve, sob que documento, com que garantia, com que cobertura e com que
acompanhamento. Uma operação pode ter score alto e crédito ruim — Torre Almirante é exatamente
isso — e pode ter score baixo e crédito excelente, sem que ninguém de fora consiga saber.

Ele existe porque, ao ler os oito termos, o padrão que apareceu não foi o risco de crédito. Foi a
desigualdade brutal de informação entre operações que estão lado a lado na mesma carteira.

## Os seis eixos

Cada eixo vale 2 (documento primário público), 1 (parcial ou de segunda mão) ou 0 (ausente).
Máximo de 12 pontos por operação.

| Eixo | Pergunta |
| --- | --- |
| Devedora | Dá para nomear a devedora e o CNPJ em documento primário? |
| Termo | O termo de securitização está em área aberta? |
| Garantias | As garantias estão descritas em documento primário, e não só listadas em cadastro? |
| Cobertura | Há LTV ou índice de cobertura publicado, e recente, para aferir a garantia? |
| Demonstrações | Há demonstrações financeiras da devedora? |
| Acompanhamento | Há relatório ou informe periódico público, e recente, da operação? |

O eixo de cobertura é o que mais pesa na prática, porque é o único que responde *se a garantia
cobre a dívida*. Os outros dizem se ela existe e como é descrita.

**Dois eixos têm prazo de validade.** Documento que descrevia a operação e não a descreve mais
não é transparência corrente: vale como evidência parcial, nunca como verificação. O prazo
acompanha a periodicidade esperada de cada eixo.

| Eixo datado | Cai para parcial a partir de | Por quê |
| --- | --- | --- |
| Cobertura | 18 meses | LTV e índice de cobertura são apurados em ciclos longos |
| Acompanhamento | 6 meses | informe é obrigação mensal; dois trimestres sem publicar não é acompanhamento |

A matriz mostra a idade da evidência ao lado da célula que envelheceu.

## Resultado

| Operação | Dev. | Termo | Gar. | Cob. | DF | Acomp. | Score |
| --- | :-: | :-: | :-: | :-: | :-: | :-: | --: |
| BRCR — Torre Almirante | ● | ● | ● | ◐ | ○ | ◐ | **67%** |
| Edifício O Parque | ● | ● | ● | ○ | ○ | ◐ | **58%** |
| HGBS — Bauru Shopping | ● | ● | ● | ○ | ○ | ◐ | **58%** |
| TRX — O Parque | ● | ● | ● | ○ | ○ | ○ | **50%** |
| Ed. Eldorado | ◐ | ● | ● | ○ | ○ | ◐ | **50%** |
| JW Marriott Hotel | ○ | ○ | ◐ | ○ | ○ | ◐ | **17%** |
| CPOF — Ed. FL Plaza | ○ | ○ | ◐ | ○ | ○ | ○ | **8%** |
| HSLG — BTS Meli | ○ | ○ | ◐ | ○ | ○ | ○ | **8%** |

● público · ◐ parcial · ○ ausente

Nenhuma célula de acompanhamento é verde. Todas as cinco operações que têm informe público estão
fora do prazo — voltamos a isso abaixo.

**Score da carteira: 38% ponderado pela exposição**, contra 40% na média simples. A ponderação
piora o número porque as duas operações mais opacas — FL Plaza e JW Marriott — são a segunda e a
terceira maiores do recorte. Quanto maior o cheque, menos se sabe.

## Os três números que importam

**R$ 392,3 mi, ou 40% do recorte, está em operações onde nem a devedora é identificável.**
FL Plaza, JW Marriott e BTS Meli. Não é que a devedora seja uma SPE opaca: é que o termo não
está em área aberta e nenhum documento público a nomeia. O investidor de KNCR11 não tem como
saber para quem emprestou dois quintos deste recorte.

**93% do recorte não tem LTV nem índice de cobertura publicado.** Só Torre Almirante permite
aferir se a garantia cobre a dívida, e essa apuração é de fevereiro de 2025.

Em uma delas, porém, deu para medir por fora: no CRI HGBS/Bauru parte da garantia são cotas de
FIIs listados, com quantidade definida no termo e preço observável. O resultado está em
[`cobertura-calculada.md`](cobertura-calculada.md). **Esse cálculo não entra no score** — o score
mede o que o mercado divulga, e trabalho próprio não torna a operação mais transparente.

**Nenhuma das oito tem demonstrações financeiras da devedora.** O eixo zerou por completo. Onde
existe DF publicada, como em Torre Almirante, ela é da emissora ou do patrimônio separado, não de
quem deve o dinheiro.

**Nenhuma das oito tem acompanhamento corrente.** Três não publicam nada; as outras cinco
publicam com atraso — o informe mais recente na área aberta tem entre 6 e 8 meses. Isso equivale
a 58% do recorte com informação periódica vencida, além dos 40% sem informação nenhuma.

## O "informe mensal" que é anual

Foi o que apareceu ao datar o eixo. Opea e VERT publicam, na área aberta do agente fiduciário, um
`Informe_Mensal` por ano, sempre referente a dezembro:

| Operação | Informes na área aberta |
| --- | --- |
| Edifício O Parque | 12/2023, 12/2024, 12/2025 |
| Ed. Eldorado | 12/2024, 12/2025 |
| JW Marriott | 12/2025 — um só |
| BRCR — Torre Almirante | 12/2024, 12/2025, além dos mensais que pararam em 02/2025 |
| HGBS — Bauru Shopping | 01/2026 e 02/2026, ambos da 1ª série; nenhum da 2ª |

O nome do documento diz mensal, a periodicidade regulatória é mensal, e a publicação é anual.
Entre março e dezembro, quem consulta está olhando dados de até doze meses atrás.

Torre Almirante é o caso mais nítido porque ali existiu acompanhamento de verdade: relatórios
mensais com LTV, índice de cobertura e status dos fundos, correndo mês a mês até fevereiro de
2025. Depois disso, silêncio — e é justamente quando o waiver do índice expirava.

## O que o score não captura

- **Frequência, não só idade.** Os prazos olham a evidência mais recente. Quem publicou uma vez em
  12/2025 e quem publicou todo mês até 12/2025 pontuam igual, embora o segundo esteja num regime
  de acompanhamento e o primeiro não.
- **Envelhecimento em degrau.** Um informe de 5 meses vale 2 e um de 6 vale 1. O corte é abrupto
  onde a realidade é contínua.
- **Qualidade do que está publicado.** O termo do Eldorado está público mas cortado, sem o Anexo
  IX. Ele pontua 2 no eixo Termo e 1 no eixo Devedora, o que só parcialmente reflete o problema.
- **Assimetria entre investidor profissional e cotista.** Boa parte destes documentos existe em
  portais fechados do agente fiduciário. O score mede a área aberta, que é o que o cotista de um
  FII listado efetivamente alcança.

## Como reproduzir

`GET /api/analytics/transparency` devolve os eixos, o score por operação e os agregados da
carteira. Os níveis vêm da leitura documentada em [`leitura-dos-termos.md`](leitura-dos-termos.md)
e estão registrados em `DemoData` — cada nível é uma afirmação verificável contra a fonte listada
no dossiê da operação.
