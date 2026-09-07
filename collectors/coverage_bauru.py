"""Cobertura do CRI HGBS/Bauru (138ª emissão da Província), calculada por fora.

O agente fiduciário não publica LTV nem índice de cobertura para esta emissão. Parte da garantia,
porém, são cotas de dois FIIs listados, cujas quantidades estão no termo de securitização — o que
torna essa parcela mensurável a preço de mercado.

A alienação fiduciária de 35% do Shopping Bauru, que parecia sem avaliação pública, também é
mensurável: o HGBS11, que é a devedora, contrata e publica no FNET um laudo por ativo do seu
portfólio. O laudo de 31/12/2025 avalia o Boulevard Shopping Bauru em R$ 270 milhões por 100% do
ativo, e a fração alienada é 35% dele.

Ficam de fora apenas os fundos de reserva e de despesas.

Uso:  python3 coverage_bauru.py
"""
from __future__ import annotations

from http_util import Session
from oliveiratrust import find_asset, outstanding_balance
from quotes import history, liquidity, quotes_session

# Quantidades empenhadas, conforme a definição "Alienação Fiduciária de Quotas e Cessão
# Fiduciária de Rendimentos" do Termo de Securitização da 138ª emissão.
PLEDGED = {"HPDP11": 282_051, "FLRP11": 1_969_834}

# Laudo Capright de 31/12/2025, publicado pelo HGBS11 no FNET, com data-base novembro de 2025.
PROPERTY_APPRAISAL = 270_000_000.00
PLEDGED_FRACTION = 0.35

# As duas séries da emissão dividem as mesmas garantias, então o denominador é o saldo somado.
SERIES = ["25L3673856", "25L3678075"]


def main() -> None:
    session = Session()
    prices = quotes_session()

    balance = 0.0
    print("Saldo devedor")
    for code in SERIES:
        asset = find_asset(session, code)
        if asset is None:
            raise SystemExit(f"série {code} não encontrada na Oliveira Trust")
        value = outstanding_balance(asset)
        balance += value
        print(f"  {code}  {asset['titulo']:<26} PU {asset['valor_pu']:>12}  saldo R$ {value:,.2f}")
    print(f"  {'total':<44} R$ {balance:,.2f}\n")

    collateral = 0.0
    print("Garantia em cotas")
    for ticker, units in PLEDGED.items():
        rows = history(prices, ticker)
        price = rows[-1][1]
        value = units * price
        collateral += value
        stats = liquidity(rows, units)
        print(f"  {ticker}  {units:>10,} cotas x R$ {price:>7,.2f} = R$ {value:>14,.2f}   ({rows[-1][0]})")
        print(f"           giro 12m {stats['total_units_traded']:>12,.0f} cotas · mediana {stats['median_units_per_day']:>8,.0f}/dia"
              f" · média {stats['mean_units_per_day']:>10,.0f}/dia")
        print(f"           posição empenhada = {stats['pledged_over_period_volume']:.2f}x o giro de 12 meses\n")

    property_value = PROPERTY_APPRAISAL * PLEDGED_FRACTION
    print(f"Garantia imobiliária")
    print(f"  Boulevard Shopping Bauru  100% = R$ {PROPERTY_APPRAISAL:,.2f}"
          f"  x {PLEDGED_FRACTION:.0%} alienado = R$ {property_value:,.2f}")
    print("           laudo Capright de 31/12/2025, data-base novembro de 2025, publicado pelo HGBS11\n")

    total = collateral + property_value
    print(f"  garantia em cotas                          R$ {collateral:,.2f}")
    print(f"  garantia imobiliária                       R$ {property_value:,.2f}")
    print(f"  garantia total                             R$ {total:,.2f}")
    print(f"  saldo devedor                              R$ {balance:,.2f}")
    print(f"  COBERTURA                                  {total / balance * 100:.0f}%")
    print(f"    só cotas                                 {collateral / balance * 100:.0f}%")
    print(f"    só imóvel                                {property_value / balance * 100:.0f}%")
    print("\n  Fora da conta: fundos de reserva e de despesas. O laudo avalia 100% do ativo e a")
    print("  fração alienada é 35%, tomada proporcionalmente — participação minoritária pode não")
    print("  valer pro rata. E preço de tela de cota não é preço de saída: ver a liquidez acima.")


if __name__ == "__main__":
    main()
