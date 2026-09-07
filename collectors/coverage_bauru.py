"""Cobertura do CRI HGBS/Bauru (138ª emissão da Província), calculada por fora.

O agente fiduciário não publica LTV nem índice de cobertura para esta emissão. Parte da garantia,
porém, são cotas de dois FIIs listados, cujas quantidades estão no termo de securitização — o que
torna essa parcela mensurável a preço de mercado.

O resultado é uma cobertura PARCIAL, e o script deixa isso explícito: fica de fora a alienação
fiduciária de 35% do Shopping Bauru, que não tem avaliação pública, e ficam de fora os fundos de
reserva e de despesas.

Uso:  python3 coverage_bauru.py
"""
from __future__ import annotations

from http_util import Session
from oliveiratrust import find_asset, outstanding_balance
from quotes import history, liquidity, quotes_session

# Quantidades empenhadas, conforme a definição "Alienação Fiduciária de Quotas e Cessão
# Fiduciária de Rendimentos" do Termo de Securitização da 138ª emissão.
PLEDGED = {"HPDP11": 282_051, "FLRP11": 1_969_834}

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

    print(f"  garantia mensurável                        R$ {collateral:,.2f}")
    print(f"  cobertura parcial                          {collateral / balance * 100:.1f}%")
    print("\n  Parcial: não inclui a alienação fiduciária de 35% do Shopping Bauru, sem avaliação")
    print("  pública, nem os fundos de reserva e de despesas. Preço de tela não é preço de saída.")


if __name__ == "__main__":
    main()
