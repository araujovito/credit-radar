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
| Cobertura | Há LTV ou índice de cobertura publicado para aferir a garantia? |
| Demonstrações | Há demonstrações financeiras da devedora? |
| Acompanhamento | Há relatório ou informe periódico público da operação? |

O eixo de cobertura é o que mais pesa na prática, porque é o único que responde *se a garantia
cobre a dívida*. Os outros dizem se ela existe e como é descrita.

## Resultado

| Operação | Dev. | Termo | Gar. | Cob. | DF | Acomp. | Score |
| --- | :-: | :-: | :-: | :-: | :-: | :-: | --: |
| BRCR — Torre Almirante | ● | ● | ● | ● | ○ | ● | **83%** |
| Edifício O Parque | ● | ● | ● | ○ | ○ | ● | **67%** |
| HGBS — Bauru Shopping | ● | ● | ● | ○ | ○ | ● | **67%** |
| Ed. Eldorado | ◐ | ● | ● | ○ | ○ | ● | **58%** |
| TRX — O Parque | ● | ● | ● | ○ | ○ | ○ | **50%** |
| JW Marriott Hotel | ○ | ○ | ◐ | ○ | ○ | ● | **25%** |
| CPOF — Ed. FL Plaza | ○ | ○ | ◐ | ○ | ○ | ○ | **8%** |
| HSLG — BTS Meli | ○ | ○ | ◐ | ○ | ○ | ○ | **8%** |

● público · ◐ parcial · ○ ausente

**Score da carteira: 43% ponderado pela exposição**, contra 46% na média simples. A ponderação
piora o número porque as duas operações mais opacas — FL Plaza e JW Marriott — são a segunda e a
terceira maiores do recorte. Quanto maior o cheque, menos se sabe.

## Os três números que importam

**R$ 392,3 mi, ou 40% do recorte, está em operações onde nem a devedora é identificável.**
FL Plaza, JW Marriott e BTS Meli. Não é que a devedora seja uma SPE opaca: é que o termo não
está em área aberta e nenhum documento público a nomeia. O investidor de KNCR11 não tem como
saber para quem emprestou dois quintos deste recorte.

**93% do recorte não tem LTV nem índice de cobertura publicado.** Só Torre Almirante permite
aferir se a garantia cobre a dívida — e essa apuração é de agosto de 2024.

**Nenhuma das oito tem demonstrações financeiras da devedora.** O eixo zerou por completo. Onde
existe DF publicada, como em Torre Almirante, ela é da emissora ou do patrimônio separado, não de
quem deve o dinheiro.

## O que o score não captura

- **Atualidade.** Torre Almirante marca 83% com uma apuração de dois anos atrás. Um documento
  velho pontua igual a um documento novo, o que superestima a operação. É a correção mais
  urgente do modelo.
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
