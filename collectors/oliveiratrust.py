"""Oliveira Trust — agente fiduciário do CRI HSLG/BTS Meli e do CRI HGBS/Bauru.

A área do investidor consome uma API aberta. A ficha de um papel traz volume, vencimento,
remuneração, as garantias descritas por extenso e — o campo que mais importa aqui — o status de
adimplência junto com as pendências que motivaram esse status.

Cuidado de leitura: o status `INADIMPLENTE` da Oliveira Trust cobre descumprimento de qualquer
obrigação, não só falha de pagamento. No BTS Meli, por exemplo, as pendências são de relatório
gerencial, relatório mensal, relatório de obras e índice financeiro.
"""
from __future__ import annotations

import json

from http_util import Session

API = "https://services-ft.oliveiratrust.com.br/app/v1"


def find_asset(session: Session, credit_code: str) -> dict | None:
    """Ficha do papel pelo código IF. `busca` é o único parâmetro que o endpoint respeita."""
    rows = session.get_json(f"{API}/titulos?busca={credit_code}").get("data", [])
    for row in rows:
        if row.get("cod_sirsan") == credit_code:
            row["detalhes"] = json.loads(row["detalhes"]) if isinstance(row.get("detalhes"), str) else row.get("detalhes")
            return row
    return None


def outstanding_balance(asset: dict) -> float:
    """Saldo devedor da série = quantidade emitida x PU corrente.

    Só vale para amortização bullet, em que não houve amortização de principal até aqui. O campo
    `amortizacao` da ficha diz qual é o caso; para papéis com amortização programada este número
    superestima o saldo.
    """
    details = asset["detalhes"]
    quantity = float(str(details["quantidade_de_ativos"]).replace(".", "").replace(",", "."))
    unit_price = float(str(details["valor_pu"]).split(" ")[0].replace(".", "").replace(",", "."))
    return quantity * unit_price
