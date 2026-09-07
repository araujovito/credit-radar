"""Cadência do acompanhamento periódico publicado em área aberta.

O eixo de acompanhamento do score olhava só a evidência mais recente. Idade não é frequência:
quem publicou uma vez em dezembro e quem publicou todos os meses até dezembro têm a mesma
"última evidência" e regimes de transparência opostos.

Aqui a data de referência de cada documento é extraída do próprio nome do arquivo, porque é o
único lugar onde ela aparece — os portais expõem data de upload, não competência.

Referência de periodicidade: o informe mensal de CRI é obrigação mensal (Suplemento E da
Resolução CVM 60), então o esperado são 12 documentos por ano.
"""
from __future__ import annotations

import datetime
import re

EXPECTED_PER_YEAR = 12

# Formatos observados nos portais, do mais específico para o mais genérico.
_PATTERNS = [
    re.compile(r"[_ (](?P<y>20\d{2})[.\-](?P<m>0[1-9]|1[0-2])[).\s_]"),   # (2025.01)  _2026-02.
    re.compile(r"Informe[_ ]Mensal[_ ](?P<m>0[1-9]|1[0-2])-(?P<y>20\d{2})"),  # Informe_Mensal_12-2025
    re.compile(r"(?<!\d)(?P<y>20\d{2})(?P<m>0[1-9]|1[0-2])(?!\d)"),       # 202312
]


def reference_date(description: str) -> datetime.date | None:
    """Data de competência do documento, deduzida do nome do arquivo."""
    for pattern in _PATTERNS:
        found = pattern.search(description)
        if found:
            return datetime.date(int(found.group("y")), int(found.group("m")), 1)
    return None


def cadence(descriptions: list[str], today: datetime.date | None = None, window_months: int = 12) -> dict:
    """Quantos documentos distintos por competência saíram na janela, e qual o mais recente.

    Conta competências distintas, não arquivos: reenvio do mesmo mês é um documento só.
    """
    today = today or datetime.date.today()
    cutoff = _months_before(today, window_months)
    months = {d for d in (reference_date(item) for item in descriptions) if d}
    in_window = {d for d in months if d >= cutoff}
    expected = round(EXPECTED_PER_YEAR * window_months / 12)
    return {
        "published_in_window": len(in_window),
        "expected_in_window": expected,
        "regularity": len(in_window) / expected if expected else None,
        "latest_reference": max(months) if months else None,
        "total_known": len(months),
    }


def _months_before(reference: datetime.date, months: int) -> datetime.date:
    month_index = reference.year * 12 + (reference.month - 1) - months
    return datetime.date(month_index // 12, month_index % 12 + 1, 1)
