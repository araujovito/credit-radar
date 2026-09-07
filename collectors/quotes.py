"""Cotações e volume de FIIs listados.

Serve para medir a garantia quando ela é composta por cotas de fundos negociados em bolsa — o
caso do CRI HGBS/Bauru. O volume importa tanto quanto o preço: uma posição empenhada que supera o
giro anual do papel tem marcação, não liquidez.
"""
from __future__ import annotations

import datetime
import statistics

from http_util import Session

CHART = "https://query1.finance.yahoo.com/v8/finance/chart/{ticker}.SA?range={range}&interval=1d"

# A fonte de cotação recusa com 429 o User-Agent completo do Chrome quando ele vem sem os
# cabeçalhos Sec-CH que um navegador real mandaria junto, e aceita um genérico. Por isso as
# cotações usam uma sessão própria, em vez da mesma dos portais de agente fiduciário.
QUOTES_UA = "Mozilla/5.0"


def quotes_session() -> Session:
    return Session(headers={"User-Agent": QUOTES_UA})


def history(session: Session, ticker: str, period: str = "1y") -> list[tuple[datetime.date, float, int]]:
    """Série diária de (data, fechamento, volume em cotas)."""
    result = session.get_json(CHART.format(ticker=ticker, range=period))["chart"]["result"][0]
    quote = result["indicators"]["quote"][0]
    rows = []
    for stamp, close, volume in zip(result["timestamp"], quote["close"], quote["volume"]):
        if close is None:
            continue
        rows.append((datetime.date.fromtimestamp(stamp), close, volume or 0))
    return rows


def liquidity(rows: list[tuple[datetime.date, float, int]], pledged_units: int) -> dict:
    """Compara a posição empenhada com o giro efetivo do papel.

    Reporta mediana e média porque a diferença entre as duas é informação: quando a média é muito
    maior que a mediana, o papel negocia em poucos blocos e fica parado no resto do tempo — o que
    muda a leitura de quanto tempo levaria para executar a garantia.
    """
    volumes = [volume for _, _, volume in rows]
    traded = sum(volumes)
    average = statistics.mean(volumes) if volumes else 0
    return {
        "sessions": len(rows),
        "median_units_per_day": statistics.median(volumes) if volumes else 0,
        "mean_units_per_day": average,
        "total_units_traded": traded,
        "pledged_units": pledged_units,
        "pledged_over_period_volume": (pledged_units / traded) if traded else None,
        "days_of_average_volume": (pledged_units / average) if average else None,
    }
