import { useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import './style.css';

type Asset={id:number;ticker:string;name:string};
type Summary={assets:number};
type Snapshot={targetAssetsPercent:number;lciPercent:number;cashPercent:number;sourceDocument:{sourceUrl:string}};
type Position={id:number;issuer:string;sector:string;indexer:string;amountMillions:number;referenceDate:string};
type Concentration={byIssuer:Record<string,number>};
type Insights={importedPositions:number;largestEconomicGroup:string;largestEconomicGroupAmountMillions:number;largestEconomicGroupPercent:number;largestSector:string};
type GroupProfile={id:number;name:string;sector:string;publicStatus:string;ticker:string;sourceUrl:string;relationshipNote:string;confidence:string};
type TAxis={key:string;label:string;question:string;recurring:boolean};
type TCadence={latestReference:string|null;ageMonths:number|null;stale:boolean;staleMonths:number;published12m:number|null;expectedPerYear:number|null;regularity:number|null;irregular:boolean;measured:boolean;basis:string};
type TOperation={slug:string;operationName:string;creditCode:string;amountMillions:number;levels:Record<string,number>;cadence:Record<string,TCadence>;points:number;percent:number;gapNote:string};
type TExposure={operations:number;amountMillions:number;percent:number;names:string[]};
type TReport={axes:TAxis[];operations:TOperation[];portfolio:{operations:number;amountMillions:number;simpleScore:number;weightedScore:number;byAxis:Record<string,TAxisSummary>;withoutDebtor:TExposure;withCoverage:TExposure;withoutTerm:TExposure;}|null};
type TAxisSummary={label:string;measured:number;unmeasured:number;published12m:number;expected12m:number;regularity:number|null};
type Coverage={slug:string;asOfDate:string;scope:string;collateralValue:number;securedBalance:number;coveragePercent:number;method:string;caveat:string;sourceNote:string};
type DebtorResearch={id:number;debtor:string;status:string;sourceUrl:string};
type CriDocument={id:number;creditCode:string;operationName:string;issuer:string;sourceType:string;availability:string;finding:string;sourceUrl:string};
type Dossier={id:number;slug:string;operationName:string;legalName:string;cnpj:string;economicGroup:string;sector:string;entityType:string;debtAndLiquidity:string;cashGeneration:string;guarantees:string;alertHistory:string;sourceNote:string;sourceUrl:string;paymentStatus:string;collateralStatus:string;collateralNote:string};

function App(){
 const [assets,setAssets]=useState<Asset[]>([]); const [summary,setSummary]=useState<Summary>(); const [snapshot,setSnapshot]=useState<Snapshot>(); const [positions,setPositions]=useState<Position[]>([]); const [concentration,setConcentration]=useState<Concentration>(); const [insights,setInsights]=useState<Insights>(); const [profiles,setProfiles]=useState<GroupProfile[]>([]); const [research,setResearch]=useState<DebtorResearch[]>([]); const [criDocuments,setCriDocuments]=useState<CriDocument[]>([]); const [dossiers,setDossiers]=useState<Dossier[]>([]); const [transparency,setTransparency]=useState<TReport>(); const [coverages,setCoverages]=useState<Coverage[]>([]);
 useEffect(()=>{Promise.all([fetch('/api/assets').then(r=>r.json()),fetch('/api/dashboard').then(r=>r.json()),fetch('/api/snapshots').then(r=>r.json()),fetch('/api/positions').then(r=>r.json()),fetch('/api/analytics/concentration?ticker=KNCR11').then(r=>r.json()),fetch('/api/analytics/insights?ticker=KNCR11').then(r=>r.json()),fetch('/api/research/groups').then(r=>r.json()),fetch('/api/research/debtors').then(r=>r.json()),fetch('/api/research/cri-documents').then(r=>r.json()),fetch('/api/debtors').then(r=>r.json()),fetch('/api/analytics/transparency').then(r=>r.json()),fetch('/api/analytics/coverage').then(r=>r.json())]).then(([a,s,p,po,c,i,g,r,d,ds,t,cv])=>{setAssets(a);setSummary(s);setSnapshot(p[0]);setPositions(po);setConcentration(c);setInsights(i);setProfiles(g);setResearch(r);setCriDocuments(d);setDossiers(ds);setTransparency(t);setCoverages(cv)})},[]);
 const selected=dossiers.find(d=>d.slug===new URLSearchParams(window.location.search).get('devedor')); if(selected)return <DossierPage dossier={selected} coverage={coverages.find(c=>c.slug===selected.slug)}/>;
 return <main><header><div><span>CREDIT RADAR</span><h1>Quem recebe o seu crédito?</h1><p>Monitoramento de crédito privado listado no Brasil.</p></div><div className="sync">● Dados até jul/2026<br/><small>primeiro ativo em análise</small></div></header>
 <section className="metrics"><Metric value={summary?.assets??0} label="ativo acompanhado"/><Metric value={insights?.importedPositions??0} label="posições atuais"/><Metric value={research.length} label="devedores investigados"/></section>
 {snapshot&&<section className="notice"><b>KNCR11 · posição de julho de 2026</b><p>{snapshot.targetAssetsPercent}% em ativos-alvo · {snapshot.lciPercent}% em LCI · {snapshot.cashPercent}% em caixa · <a href={snapshot.sourceDocument.sourceUrl} target="_blank">documento na B3 ↗</a></p></section>}
 {insights&&<section className="notice"><b>Leitura automática da carteira importada</b><p>O grupo {insights.largestEconomicGroup} concentra R$ {insights.largestEconomicGroupAmountMillions.toLocaleString('pt-BR')} mi ({insights.largestEconomicGroupPercent}%) das posições importadas. O maior setor é {insights.largestSector}.</p></section>}
 <section><div className="title"><div><span className="eyebrow">ATIVO EM ANÁLISE</span><h2>{assets[0]?.ticker} · {assets[0]?.name}</h2></div><button>Importar relatório</button></div></section>
 <section><div className="title"><div><span className="eyebrow">CARTEIRA DE DEVEDORES · JUL/2026</span><h2>CRIs importados do relatório</h2></div></div><div className="assets">{positions.filter(p=>p.referenceDate==='2026-07-31').map(p=>{const r=research.find(item=>item.debtor===p.issuer);return <article key={p.id}><div className="badge">CRI</div><div><b>{p.issuer}</b><small>{p.sector} · {p.indexer}</small></div><span className="type">R$ {p.amountMillions.toLocaleString('pt-BR')} mi</span><a className="manager" href={r?.sourceUrl} target="_blank">{r?.status??'PENDENTE'} ↗</a><i>→</i></article>})}</div>{concentration&&<section className="notice"><b>Concentração por devedor</b><p>{Object.entries(concentration.byIssuer).filter(([issuer])=>issuer!=="Hotel Hilton Copacabana"&&issuer!=="JSRE — Ed. Rochaverá").map(([issuer,value])=>`${issuer}: R$ ${value.toLocaleString('pt-BR')} mi`).join(' · ')}</p></section>}</section>
 <section><div className="title"><div><span className="eyebrow">DOCUMENTOS DAS OPERAÇÕES</span><h2>O que já foi confirmado</h2></div></div><p className="section-note">Toque em qualquer parte da linha para abrir a ficha de crédito do devedor.</p><div className="document-list">{criDocuments.map(d=>{const dossier=dossiers.find(item=>item.operationName===d.operationName);const href=dossier?`?devedor=${dossier.slug}`:d.sourceUrl;return <article className={`document-card ${dossier?'clickable':''}`} key={d.id} onClick={()=>{if(dossier)window.location.assign(href)}}><div className="badge">{d.creditCode}</div><div><b>{d.operationName}</b><small>{d.issuer} · {d.sourceType}</small><p>{d.finding}</p></div><span className="status-pair"><em className={`chip pay ${dossier?slugify(dossier.paymentStatus):''}`}>Pagamento: {dossier?dossier.paymentStatus:d.availability}</em><em className={`chip col ${dossier?slugify(dossier.collateralStatus):''}`}>Garantia: {dossier?dossier.collateralStatus:'SEM INFORMAÇÃO'}</em></span>{dossier?<a className="manager" href={href}>Ver devedor ↗</a>:<a className="manager" href={href} target="_blank">Abrir fonte ↗</a>}<a className="open-arrow" href={href} target={dossier?undefined:"_blank"} aria-label={`Abrir ${d.operationName}`}>→</a></article>})}</div></section>
 {transparency?.portfolio&&<TransparencySection report={transparency}/>}
 <section className="notice split-note"><b>Pagamento em dia não é garantia saudável</b><p>Das {dossiers.length} operações com dossiê, {dossiers.filter(d=>d.paymentStatus==='ADIMPLENTE').length} constam como adimplentes em pagamento, mas só {dossiers.filter(d=>d.collateralStatus==='SEM PENDÊNCIA').length} está sem pendência de garantia. {dossiers.filter(d=>d.collateralStatus==='PENDÊNCIAS ABERTAS').length} têm apurações de razão ou formalização de garantia em aberto, {dossiers.filter(d=>d.collateralStatus==='SOB WAIVER').length} opera sob waiver de índice, e {dossiers.filter(d=>d.collateralStatus==='SEM INFORMAÇÃO').length} não têm relatório de garantias público para medir cobertura.</p></section>
 <section><div className="title"><div><span className="eyebrow">PESQUISA PÚBLICA</span><h2>Grupos econômicos prioritários</h2></div></div><div className="assets">{profiles.map(g=><article key={g.id}><div className="badge">{g.confidence}</div><div><b>{g.name} {g.ticker&&`· ${g.ticker}`}</b><small>{g.sector} · {g.publicStatus}</small></div><span className="type">{g.relationshipNote}</span><a className="manager" href={g.sourceUrl} target="_blank">Fonte pública ↗</a><i>→</i></article>)}</div></section></main>
}


function money(v:number){return 'R$ '+v.toLocaleString('pt-BR',{minimumFractionDigits:2,maximumFractionDigits:2})}
function CoverageBlock({c}:{c:Coverage}){
 return <section className="coverage-block">
  <h2>Cobertura calculada pelo Credit Radar</h2>
  <p className="coverage-scope">{c.scope} · data-base {new Date(c.asOfDate+'T12:00:00').toLocaleDateString('pt-BR')}</p>
  <div className="coverage-figures">
   <div><span>Garantia mensurável</span><b>{money(c.collateralValue)}</b></div>
   <div><span>Saldo devedor coberto</span><b>{money(c.securedBalance)}</b></div>
   <div className="pct"><span>Cobertura parcial</span><b>{c.coveragePercent}%</b></div>
  </div>
  <h3>Como foi calculado</h3><p>{c.method}</p>
  <h3>Por que o número não é um valor de saída</h3><p className="caveat">{c.caveat}</p>
  <p className="coverage-source">{c.sourceNote}</p>
  <p className="coverage-source">Este cálculo não conta para o score de transparência: o score mede o que o mercado divulga, e cálculo próprio não torna a operação mais transparente.</p>
 </section>;
}
const LEVELS=['ausente','parcial','publico'];
const LEVEL_LABEL=['Ausente','Parcial','Público'];

function Cell({axis,operation}:{axis:TAxis;operation:TOperation}){
 const level=operation.levels[axis.key]; const c=operation.cadence[axis.key];
 const when=c&&c.latestReference?new Date(c.latestReference+'T12:00:00').toLocaleDateString('pt-BR'):null;
 const mark=!c?null:c.measured?`${c.published12m}/${c.expectedPerYear}`:(c.stale?`${c.ageMonths}m`:null);
 const why=!c?'':(c.measured
   ?`${c.published12m} de ${c.expectedPerYear} competências em 12 meses${when?`; a mais recente refere-se a ${when}`:''}. ${c.basis}`
   :`Cadência não medida. ${c.basis}`);
 return <em className={`cell ${LEVELS[level]}${c&&!c.measured&&axis.recurring?' unmeasured':''}`} title={`${axis.label}: ${LEVEL_LABEL[level]}${why?` — ${why}`:''} — ${axis.question}`}>
  {LEVEL_LABEL[level]}{mark?<i className="stale"> · {mark}</i>:null}
 </em>;
}
function TransparencySection({report}:{report:TReport}){
 const p=report.portfolio!;
 return <section className="transparency"><div className="title"><div><span className="eyebrow">SCORE DE TRANSPARÊNCIA · KNCR11</span><h2>O que dá para saber, e o que não dá</h2></div></div>
 <p className="section-note">O score não mede a qualidade do crédito. Mede quanto da operação é verificável em fonte aberta: quem deve, sob que documento, com que garantia, com que cobertura e com que acompanhamento.</p>
 <div className="score-head">
  <div className="score-big"><strong>{p.weightedScore}%</strong><span>ponderado pela exposição</span><small>{p.simpleScore}% na média simples · {p.operations} operações · R$ {p.amountMillions.toLocaleString('pt-BR')} mi</small></div>
  <div className="score-facts">
   <div className="fact bad"><b>R$ {p.withoutDebtor.amountMillions.toLocaleString('pt-BR')} mi · {p.withoutDebtor.percent}%</b><span>em operações onde nem a devedora é identificável em fonte aberta</span><small>{p.withoutDebtor.names.join(' · ')}</small></div>
   {report.axes.filter(a=>a.recurring&&p.byAxis[a.key]&&p.byAxis[a.key].measured>0).map(a=>{const x=p.byAxis[a.key];return (
    <div className="fact bad" key={a.key}><b>{x.published12m} de {x.expected12m} · {x.regularity}%</b>
    <span>{a.label.toLowerCase()}: o que foi publicado em doze meses, contra o exigido</span>
    <small>medido em {x.measured} de {p.operations} operações{x.unmeasured>0?` · nas outras ${x.unmeasured} a periodicidade exigida não está em fonte aberta`:''}</small></div>)})}
   <div className="fact bad"><b>{100-p.withCoverage.percent}% do recorte</b><span>sem LTV ou índice de cobertura publicado</span><small>Só {p.withCoverage.names.join(' · ')} tem apuração, e ela venceu: caiu para parcial por ter mais de 18 meses</small></div>
  </div>
 </div>
 <div className="matrix" role="table">
  <div className="matrix-row head" role="row"><span>Operação</span>{report.axes.map(a=><em key={a.key} title={a.question}>{a.label}</em>)}<span className="score-col">Score</span></div>
  {report.operations.map(o=><div className="matrix-row" role="row" key={o.slug}>
   <span className="op"><b>{o.operationName}</b><small>{o.creditCode} · R$ {o.amountMillions.toLocaleString('pt-BR')} mi</small></span>
   {report.axes.map(a=><Cell key={a.key} axis={a} operation={o}/>)}
   <span className={`score-col ${o.percent>=67?'ok':o.percent>=40?'warn':'bad'}`}>{o.percent}%</span>
  </div>)}
 </div>
 <div className="gaps">{report.operations.map(o=><div className="gap" key={o.slug}><b>{o.operationName} · {o.percent}%</b><p>{o.gapNote}</p></div>)}</div>
 </section>;
}
function slugify(v:string){return (v||'').toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g,'').replace(/[^a-z]+/g,'-')}
function Metric({value,label}:{value:number;label:string}){return <div className="metric"><strong>{value}</strong><span>{label}</span></div>}
function DossierPage({dossier:d,coverage}:{dossier:Dossier;coverage?:Coverage}){return <main><header><div><span>DOSSIÊ DE CRÉDITO · KNCR11</span><h1>{d.operationName}</h1><p>Informações confirmadas em fontes públicas e pendências documentais.</p></div><a className="back" href="/">← Voltar ao painel</a></header><section className="dossier-grid"><Info label="Nome jurídico" value={d.legalName}/><Info label="CNPJ" value={d.cnpj}/><Info label="Grupo econômico" value={d.economicGroup}/><Info label="Setor e segmento" value={d.sector}/><Info label="Estrutura" value={d.entityType}/></section><DossierBlock title="Dívida e liquidez" value={d.debtAndLiquidity}/><DossierBlock title="Geração de caixa e demonstrações" value={d.cashGeneration}/><DossierBlock title="Garantias do CRI" value={d.guarantees}/><section className="status-split"><div className={`status-card pay ${slugify(d.paymentStatus)}`}><span>Adimplência de pagamento</span><b>{d.paymentStatus}</b><p>Refere-se apenas ao pagamento de juros e amortização no vencimento.</p></div><div className={`status-card col ${slugify(d.collateralStatus)}`}><span>Saúde da garantia</span><b>{d.collateralStatus}</b><p>{d.collateralNote}</p></div></section>{coverage&&<CoverageBlock c={coverage}/>}<DossierBlock title="Histórico de alertas e eventos relevantes" value={d.alertHistory}/><section className="notice"><b>Fontes primárias</b><p>{d.sourceNote} <a href={d.sourceUrl} target="_blank">Abrir fonte ↗</a></p></section></main>}
function Info({label,value}:{label:string;value:string}){return <div className="info"><span>{label}</span><b>{value}</b></div>}; function DossierBlock({title,value}:{title:string;value:string}){return <section className="dossier-block"><h2>{title}</h2><p>{value}</p></section>}
createRoot(document.getElementById('root')!).render(<App/>);
