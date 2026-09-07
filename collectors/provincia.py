"""Companhia Província de Securitização — emissora do CRI HGBS/Bauru (25L3678075).

Portal ASP.NET WebForms. A grade de emissões vem inteira no HTML; o detalhe fica em
`Detalheemissao?id=<id interno>`; e o termo só sai de um postback que seleciona o tipo de anexo e
devolve o link para o FileHandler. Nenhuma dessas URLs é descobrível sem passar pelo formulário.
"""
from __future__ import annotations

import re
import urllib.parse

from http_util import Session

SITE = "https://provinciasecuritizadora.com.br"
GRID = f"{SITE}/GridSmartAdmin?id=2"  # id=2 é a grade de CRI


def find_emission_id(session: Session, credit_code: str) -> int | None:
    """Localiza o id interno da emissão a partir do código IF listado na grade."""
    html = session.get_text(GRID)
    position = html.find(credit_code)
    if position < 0:
        return None
    before = html[:position]
    ids = re.findall(r'name="rtpEmissoes\$ctl\d+\$hidID" id="[^"]*" value="(\d+)"', before)
    return int(ids[-1]) if ids else None


def attachments(session: Session, emission_id: int) -> dict[str, str]:
    """Mapeia nome do anexo para o valor que o postback espera em `ddlTipoanexo`."""
    html = session.get_text(f"{SITE}/Detalheemissao?id={emission_id}")
    block = re.search(r"(?s)<select[^>]*ddlTipoanexo.*?</select>", html)
    if not block:
        return {}
    out = {}
    for value, label in re.findall(r'<option value="(\d+)">\s*(.*?)</option>', block.group(0), re.S):
        out[_unescape(label).strip()] = value
    return out


def attachment_url(session: Session, emission_id: int, attachment_value: str) -> str | None:
    """Executa o postback e devolve a URL absoluta do PDF servido pelo FileHandler."""
    page_url = f"{SITE}/Detalheemissao?id={emission_id}"
    html = session.get_text(page_url)
    fields = {
        "__EVENTTARGET": "ctl00$ContentPlaceHolder1$SalvarDataDownload",
        "__EVENTARGUMENT": "",
        "ctl00$ContentPlaceHolder1$ddlTipoanexo": attachment_value,
    }
    for name in ("__VIEWSTATE", "__VIEWSTATEGENERATOR", "__EVENTVALIDATION"):
        found = re.search(rf'id="{name}" value="([^"]*)"', html)
        fields[name] = found.group(1) if found else ""
    body = session.post_form(page_url, fields).decode("utf-8", "replace")
    link = re.search(r'href="(Handlers/FileHandler\.ashx\?file=[^"]+)"', body)
    if not link:
        return None
    path = _unescape(link.group(1)).split("file=", 1)[1]
    return f"{SITE}/Handlers/FileHandler.ashx?file=" + urllib.parse.quote(path, safe="")


def _unescape(value: str) -> str:
    import html as html_module

    return html_module.unescape(value)
