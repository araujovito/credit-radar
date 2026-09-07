"""Vórtx — agente fiduciário de Torre Almirante, Eldorado, O Parque, JW Marriott e FL Plaza.

A busca do site resolve o código IF para o id numérico interno, que é o que todas as demais
rotas exigem. Foi esse passo que revelou que Torre Almirante e Eldorado estavam na Vórtx, e não
nos portais onde este projeto os procurava antes.

As URLs dos documentos são assinadas e expiram, então download e extração precisam acontecer na
mesma execução em que a listagem foi obtida.
"""
from __future__ import annotations

import re

from http_util import Session

SITE = "https://www.vortx.com.br"
API = "https://apis.vortx.com.br/vxsite/api"


def find_operation_ids(session: Session, term: str) -> list[int]:
    """Resolve um código IF ou nome de operação para os ids internos da Vórtx."""
    html = session.get_text(f"{SITE}/investidor/dcm?busca={term.replace(' ', '+')}")
    return sorted({int(m) for m in re.findall(r"operacao\?id=(\d+)", html)})


def summary(session: Session, operation_id: int) -> dict:
    """Emissora, série, volume, indexador, vencimento, garantias cadastradas, status de
    inadimplemento e a lista de obrigações vencidas em aberto — a fonte do selo de garantia."""
    return session.get_json(f"{API}/operacao/{operation_id}/resumo")


def documents(session: Session, operation_id: int) -> dict[str, list[dict]]:
    """Documentos agrupados por tipo: TS, contratos, notificações, relatórios, laudos."""
    groups = session.get_json(f"{API}/operacao/{operation_id}/documentos-por-tipo/2")
    return {g["type"]: g["documents"] for g in groups}


def annual_reports(session: Session, operation_id: int) -> list[dict]:
    return session.get_json(f"{API}/operacao/{operation_id}/relatorios-anuais").get("data", [])


def pending_obligations_by_category(summary_payload: dict) -> dict[str, int]:
    """Agrupa as obrigações vencidas por categoria.

    A distinção importa: 'Relatórios Periódicos' e 'Demonstrações Financeiras' costumam ser dever
    da emissora, enquanto 'Razão de Garantia' e 'Formalização de Garantia' recaem sobre o devedor
    e sobre a cobertura. Somar tudo em um número só apaga exatamente o que interessa ao crédito.
    """
    out: dict[str, int] = {}
    for item in summary_payload.get("obrigacoesVencidas", []):
        out[item["categoria"]] = out.get(item["categoria"], 0) + 1
    return dict(sorted(out.items(), key=lambda kv: -kv[1]))
