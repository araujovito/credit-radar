# Cobertura calculada — CRI HGBS/Bauru

Sete das oito operações do recorte não têm LTV nem índice de cobertura publicado. Em uma delas
deu para medir por fora, e o resultado mostra por que "não publicado" não é o mesmo que
"incalculável" — nem, como se vê no fim, o mesmo que "resolvido".

Reprodução: `python3 collectors/coverage_bauru.py`.

## Por que essa operação e não outra

A garantia do CRI da 138ª emissão da Província inclui **alienação fiduciária de cotas de dois FIIs
listados**, com as quantidades definidas no próprio termo de securitização:

- 282.051 cotas do **HPDP11** (Hedge Shopping Parque Dom Pedro FII)
- 1.969.834 cotas do **FLRP11** (Hedge Floripa Shopping FII)

Quantidade em documento primário mais preço observável em bolsa dão um valor de garantia. Nas
outras sete, a garantia é imóvel sem avaliação pública — não há como medir sem laudo.

## O cálculo

Data-base 04/09/2026, mesma data do último preço unitário divulgado para os CRI.

**Saldo devedor.** As duas séries da emissão dividem as mesmas garantias, então o denominador é a
soma. Ambas são bullet, sem amortização de principal até aqui, o que torna válido multiplicar
quantidade por PU.

| Série | Código | Quantidade | PU | Saldo |
| --- | --- | --: | --: | --: |
| 1ª | 25L3673856 | 26.369 | R$ 1.055,79 | R$ 27.840.132,12 |
| 2ª | 25L3678075 | 65.086 | R$ 1.055,23 | R$ 68.680.973,14 |
| | | | **total** | **R$ 96.521.105,26** |

**Garantia em cotas**, a preço de fechamento do mesmo dia:

| Papel | Cotas empenhadas | Preço | Valor |
| --- | --: | --: | --: |
| HPDP11 | 282.051 | R$ 88,00 | R$ 24.820.488,00 |
| FLRP11 | 1.969.834 | R$ 19,99 | R$ 39.376.981,21 |
| | | **total** | **R$ 64.197.469,21** |

**Cobertura parcial: 66,5%.**

Parcial porque fica de fora a alienação fiduciária de 35% do Shopping Bauru, que não tem avaliação
pública, e ficam de fora os fundos de reserva e de despesas. A cobertura total é necessariamente
maior — só não dá para saber quanto.

## O problema que o número esconde

Preço de tela não é preço de saída, e aqui a distância é grande.

| Papel | Cotas empenhadas | Giro em 12 meses | Mediana/dia | Média/dia |
| --- | --: | --: | --: | --: |
| HPDP11 | 282.051 | 6.772.895 | 13 | 26.984 |
| FLRP11 | 1.969.834 | 323.151 | 547 | 1.287 |

**As cotas de FLRP11 empenhadas equivalem a 6,1 vezes todo o giro do papel em doze meses.** Elas
são 61% da garantia mensurável. Executar essa parte da garantia não é vender uma posição: é
inventar um mercado que não existe.

O HPDP11 é menos crítico em tamanho — a posição empenhada é 4,2% do giro anual — mas a distância
entre mediana de 13 cotas por dia e média de 26.984 diz que o papel negocia em pouquíssimos
blocos e fica parado no resto do tempo. O preço de referência sai de um livro raso.

## Como ler isso

O número correto para o dossiê não é "66,5% de cobertura". É: **a parcela mensurável da garantia
cobre 66,5% do saldo a preço de tela, e 61% dessa parcela está em um papel cujo giro anual inteiro
é um sexto da posição dada em garantia.**

Vale registrar também o que a estrutura significa. A devedora é o HGBS11 e a garantia são cotas de
outros dois FIIs do mesmo gestor, de titularidade da própria devedora. Garantia e devedora
compartilham gestor, setor — shoppings — e, em boa medida, o mesmo ciclo. Isso não é defeito
oculto: está no termo. Mas não aparece em nenhuma classificação de risco da operação.

## Este cálculo não entra no score de transparência

O score mede o que o mercado divulga. Trabalho próprio não torna a operação mais transparente para
quem não o fez, e inflar o score com ele apagaria justamente o que o score serve para mostrar. No
painel a cobertura calculada aparece em bloco separado, com data-base e método à vista.
