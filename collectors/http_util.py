"""Cliente HTTP mínimo, só com a biblioteca padrão.

Os portais de agente fiduciário são aplicações JavaScript: a página renderizada não serve como
fonte. Todo coletor aqui vai à camada de dados — payload embutido no HTML, API interna do site
ou postback de formulário — e devolve dados crus, sem interpretação.
"""
from __future__ import annotations

import gzip
import json
import time
import urllib.error
import urllib.parse
import urllib.request
from http.cookiejar import CookieJar

UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0 Safari/537.36"
TIMEOUT = 90


class Session:
    """Mantém cookies entre chamadas — a Província precisa disso para o postback."""

    def __init__(self, headers: dict[str, str] | None = None):
        self._opener = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(CookieJar()))
        self._headers = {"User-Agent": UA, "Accept": "*/*", "Accept-Language": "pt-BR,pt;q=0.9"}
        self._headers.update(headers or {})

    def get_bytes(self, url: str, headers: dict[str, str] | None = None, attempts: int = 4) -> bytes:
        """Alguns portais respondem 429 ou 5xx sob rajada; recua e tenta de novo."""
        req = urllib.request.Request(url, headers={**self._headers, **(headers or {})})
        for attempt in range(attempts):
            try:
                with self._opener.open(req, timeout=TIMEOUT) as resp:
                    raw = resp.read()
                return gzip.decompress(raw) if raw[:2] == b"\x1f\x8b" else raw
            except urllib.error.HTTPError as error:
                retriable = error.code == 429 or 500 <= error.code < 600
                if not retriable or attempt == attempts - 1:
                    raise
                time.sleep(2 ** attempt)
        raise RuntimeError("inalcançável")

    def get_text(self, url: str, headers: dict[str, str] | None = None) -> str:
        return self.get_bytes(url, headers).decode("utf-8", "replace")

    def get_json(self, url: str, headers: dict[str, str] | None = None):
        return json.loads(self.get_text(url, headers))

    def post_form(self, url: str, fields: dict[str, str], headers: dict[str, str] | None = None) -> bytes:
        body = urllib.parse.urlencode(fields).encode()
        req = urllib.request.Request(
            url, body,
            headers={**self._headers, "Content-Type": "application/x-www-form-urlencoded",
                     "Referer": url, **(headers or {})})
        with self._opener.open(req, timeout=TIMEOUT) as resp:
            return resp.read()
