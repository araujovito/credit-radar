"""Fundos.NET (FNET/B3) — documentos regulatórios de fundos.

Serve para o que o dossiê da operação não traz: quando a devedora de um CRI é um FII, as
demonstrações financeiras, os laudos de avaliação e os informes dela existem aqui, no registro do
próprio fundo, e não no portal do agente fiduciário da operação.

Duas armadilhas, ambas custaram tempo:

1. O filtro de CNPJ chama-se ``cnpjFundo``. O formulário também manda um campo ``cnpj``, que o
   servidor aceita e **ignora** — a contagem de resultados não muda. Filtrar pelo campo errado
   devolve a base inteira e parece que o filtro não existe.
2. ``palavraChave`` responde HTTP 500. Não há busca textual utilizável.

O escopo é FII, FIDC e ETF. CRI não é alcançável pelo CNPJ da securitizadora: consultar a Riza
por ``cnpjFundo`` devolve zero em todos os tipos de fundo.
"""
from __future__ import annotations

import urllib.parse

from http_util import Session

BASE = "https://fnet.bmfbovespa.com.br/fnet/publico/"
FII, FIDC = "1", "2"


def documents(session: Session, cnpj: str, kind: str = FII, limit: int = 100) -> list[dict]:
    """Documentos entregues pelo fundo, do mais recente para o mais antigo.

    ``cnpj`` sem pontuação. A sessão precisa abrir o gerenciador antes: a consulta depende do
    cookie que a página inicial cria.
    """
    session.get_text(BASE + "abrirGerenciadorDocumentosCVM")
    params = {
        "d": "0", "s": "0", "l": str(limit), "o[0][dataEntrega]": "desc",
        "tipoFundo": kind, "administrador": "", "idFundo": "",
        "idCategoriaDocumento": "0", "idTipoDocumento": "0", "idEspecieDocumento": "0",
        "situacao": "A", "cnpj": "", "cnpjFundo": cnpj,
    }
    payload = session.get_json(
        BASE + "pesquisarGerenciadorDocumentosDados?" + urllib.parse.urlencode(params),
        {"X-Requested-With": "XMLHttpRequest", "Referer": BASE + "abrirGerenciadorDocumentosCVM"})
    return payload.get("data", [])


def financial_statements(session: Session, cnpj: str) -> list[dict]:
    """Demonstrações financeiras do fundo, da mais recente para a mais antiga."""
    rows = [d for d in documents(session, cnpj)
            if "Demonstra" in ((d.get("categoriaDocumento") or "") + (d.get("tipoDocumento") or ""))]
    return sorted(rows, key=lambda d: d.get("dataReferencia") or "", reverse=True)


def appraisals(session: Session, cnpj: str) -> list[dict]:
    """Laudos de avaliação. Um FII de shoppings publica um por ativo, na virada do exercício."""
    return [d for d in documents(session, cnpj, limit=200)
            if "Laudo" in (d.get("categoriaDocumento") or "")]


def download(session: Session, document_id: int) -> bytes:
    return session.get_bytes(f"{BASE}downloadDocumento?id={document_id}")
