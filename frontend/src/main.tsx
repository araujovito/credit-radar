import { FormEvent, useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import './style.css';

type Category = { id: number; name: string; color: string };
type Item = { id:number; description:string; amount:number; type:'INCOME'|'EXPENSE'; occurredOn:string; category:Category };
type Summary = { income:number; expense:number; balance:number; expensesByCategory: Record<string,number> };
const api = '/api';
const money = new Intl.NumberFormat('pt-BR',{style:'currency',currency:'BRL'});

function App() {
  const [categories,setCategories]=useState<Category[]>([]); const [items,setItems]=useState<Item[]>([]); const [summary,setSummary]=useState<Summary>();
  const [form,setForm]=useState({description:'',amount:'',type:'EXPENSE',categoryId:'',occurredOn:new Date().toISOString().slice(0,10)});
  const load=async()=>{ const [c,t,s]=await Promise.all([fetch(`${api}/categories`),fetch(`${api}/transactions`),fetch(`${api}/dashboard`)]); setCategories(await c.json()); setItems(await t.json()); setSummary(await s.json()); };
  useEffect(()=>{load()},[]);
  const submit=async(e:FormEvent)=>{e.preventDefault(); await fetch(`${api}/transactions`,{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({...form,amount:Number(form.amount),categoryId:Number(form.categoryId)})}); setForm({...form,description:'',amount:''}); load();};
  const remove=async(id:number)=>{await fetch(`${api}/transactions/${id}`,{method:'DELETE'});load()};
  return <main><header><div><span>DEVFINANCE</span><h1>Seu mês em números.</h1><p>Um projeto full stack para acompanhar receitas e despesas.</p></div><strong>{money.format(summary?.balance ?? 0)}</strong></header>
  <section className="cards"><Card label="Receitas" value={summary?.income} accent="green"/><Card label="Despesas" value={summary?.expense} accent="red"/><Card label="Saldo" value={summary?.balance} accent="blue"/></section>
  <section className="grid"><form onSubmit={submit}><h2>Novo lançamento</h2><label>Descrição<input required value={form.description} onChange={e=>setForm({...form,description:e.target.value})}/></label><label>Valor<input required min="0.01" step="0.01" type="number" value={form.amount} onChange={e=>setForm({...form,amount:e.target.value})}/></label><div className="row"><label>Tipo<select value={form.type} onChange={e=>setForm({...form,type:e.target.value})}><option value="EXPENSE">Despesa</option><option value="INCOME">Receita</option></select></label><label>Data<input type="date" value={form.occurredOn} onChange={e=>setForm({...form,occurredOn:e.target.value})}/></label></div><label>Categoria<select required value={form.categoryId} onChange={e=>setForm({...form,categoryId:e.target.value})}><option value="">Selecione</option>{categories.map(c=><option key={c.id} value={c.id}>{c.name}</option>)}</select></label><button>Adicionar lançamento</button></form>
  <div className="panel"><h2>Despesas por categoria</h2>{Object.entries(summary?.expensesByCategory ?? {}).map(([name,value])=><div className="category" key={name}><span>{name}</span><b>{money.format(value)}</b></div>)}</div></section>
  <section className="panel"><h2>Movimentações recentes</h2>{items.map(item=><article key={item.id}><i style={{background:item.category.color}}></i><span><b>{item.description}</b><small>{item.category.name} · {new Date(item.occurredOn+'T00:00:00').toLocaleDateString('pt-BR')}</small></span><em className={item.type}>{item.type==='INCOME'?'+':'-'} {money.format(item.amount)}</em><button className="delete" onClick={()=>remove(item.id)}>×</button></article>)}</section></main>
}
function Card({label,value,accent}:{label:string;value?:number;accent:string}){return <div className={`card ${accent}`}><span>{label}</span><strong>{money.format(value??0)}</strong></div>}
createRoot(document.getElementById('root')!).render(<App/>);
