"""BariSec — emissora do CRI Sírio-Libanês (código IF 26E4046156).

A página `/emissoes/<código IF>` é Next.js e embute o payload do servidor no próprio HTML, com os
metadados da emissão e as URLs diretas dos PDFs. Ler o HTML como texto renderizado devolve nada;
ler o payload devolve tudo.
"""
from __future__ import annotations

import json
import re

from http_util import Session

SITE = "https://barisec.com.br"


def emission(session: Session, credit_code: str) -> dict:
    """Devolve {'emission': {...}, 'documents': [...]} da página pública da emissão."""
    html = session.get_text(f"{SITE}/emissoes/{credit_code}")
    payload = _next_payload(html)
    props = payload["props"]["pageProps"]
    return {
        "emission": props["emission"],
        "documents": [
            {
                "name": doc["documentName"],
                "type": doc["documentType"],
                "date": doc["date"],
                "url": doc["file"]["url"],
            }
            for doc in props.get("emissionCrisDocuments", [])
        ],
        "financial_statements": props.get("emissionFinancialStatements", []),
    }


def _next_payload(html: str) -> dict:
    """O payload é o maior objeto JSON do HTML; a marcação exata do Next muda entre versões."""
    match = re.search(r'\{"props":\{"pageProps".*?\}(?=</script>|\s*$)', html, re.S)
    if not match:
        raise LookupError("payload do Next.js não encontrado na página da emissão")
    return json.loads(match.group(0))
