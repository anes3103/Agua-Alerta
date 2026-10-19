/* app.js — interface (DOM, eventos, validação). Chamadas à API ficam em api.js */

// Nomes de exibição do enum StatusDenuncia do back-end
const STATUS = {
  PENDENTE: "Pendente", EM_ANALISE: "Em análise", EM_ANDAMENTO: "Em andamento",
  RESOLVIDA: "Resolvida", CANCELADA: "Cancelada"
};
// Tipos que vêm do INSERT de database/schema.sql. Só servem para pré-visualizar o formulário
// enquanto a API de tipos não existe; o envio usa o id retornado pela API.
const TIPOS_SCHEMA = [
  "Vazamento de água", "Esgoto a céu aberto", "Água contaminada ou alteração na qualidade",
  "Falta de água ou desabastecimento", "Poluição de rios e mananciais"
];

const $ = (sel) => document.querySelector(sel);

/* ---------- Mensagens ---------- */
function mostrarMensagem(texto, tipo = "") {
  const el = $("#mensagem");
  el.textContent = texto;
  el.className = "mensagem " + tipo;
  el.hidden = false;
}
const limparMensagem = () => { $("#mensagem").hidden = true; };

function erroAmigavel(e) {
  return e && e.message ? e.message : "Algo deu errado. Tente novamente.";
}

/* ---------- Navegação por # ---------- */
const PAGINAS = ["inicio", "denuncias", "nova", "conta", "perfil"];

function navegar() {
  const alvo = PAGINAS.includes(location.hash.slice(1)) ? location.hash.slice(1) : "inicio";
  PAGINAS.forEach((p) => { $("#" + p).hidden = p !== alvo; });
  document.querySelectorAll("nav a").forEach((a) => a.classList.toggle("ativo", a.hash === "#" + alvo));
  $("#menu").classList.remove("aberto");
  limparMensagem();
  if (alvo === "denuncias") carregarDenuncias();
  if (alvo === "nova") carregarTipos();
  if (alvo === "perfil") carregarPerfil();
  window.scrollTo(0, 0);
}

/* ---------- Denúncias: lista (GET listarDenuncias) ---------- */
async function carregarDenuncias() {
  const lista = $("#lista");
  lista.innerHTML = '<p class="vazio">Carregando denúncias...</p>';
  try {
    const dados = await listarDenuncias();
    if (!dados || dados.length === 0) {
      lista.innerHTML = '<p class="vazio">Nenhuma denúncia registrada ainda. <a href="#nova">Seja o primeiro a registrar.</a></p>';
      return;
    }
    lista.innerHTML = "";
    dados.forEach((d) => lista.appendChild(criarCard(d)));
  } catch (e) {
    lista.innerHTML = "";
    mostrarMensagem(erroAmigavel(e), "erro");
  }
}

function textoLocal(loc) {
  if (!loc) return "Local não informado";
  return [loc.endereco, loc.bairro, loc.cidade && loc.estado ? `${loc.cidade}/${loc.estado}` : loc.cidade]
    .filter(Boolean).join(", ") || "Local não informado";
}

function criarCard(d) {
  const card = document.createElement("article");
  card.className = "card";
  const status = document.createElement("span");
  status.className = "status " + d.status;
  status.textContent = STATUS[d.status] || d.status;
  const titulo = document.createElement("h3");
  titulo.textContent = d.titulo || "Sem título";
  const tipo = document.createElement("small");
  tipo.textContent = d.tipoOcorrencia ? d.tipoOcorrencia.nome : "";
  const local = document.createElement("small");
  local.textContent = textoLocal(d.localizacao);
  const botao = document.createElement("button");
  botao.className = "btn sec";
  botao.textContent = "Ver detalhes";
  botao.addEventListener("click", () => abrirDetalhe(d.id));
  card.append(status, titulo, tipo, local, botao);
  return card;
}

/* ---------- Denúncias: detalhe (GET buscarDenuncia) ---------- */
async function abrirDetalhe(id) {
  try {
    const d = await buscarDenuncia(id);
    const dlg = $("#detalhe");
    dlg.innerHTML = "";
    const h = document.createElement("h2");
    h.textContent = d.titulo || "Sem título";
    const info = document.createElement("p");
    info.className = "meta";
    const data = d.data ? new Date(d.data).toLocaleDateString("pt-BR") : "";
    info.textContent = [STATUS[d.status] || d.status, d.tipoOcorrencia && d.tipoOcorrencia.nome, data].filter(Boolean).join(" | ");
    const desc = document.createElement("p");
    desc.textContent = d.descricao;
    const loc = document.createElement("p");
    const l = d.localizacao;
    loc.textContent = "Local: " + textoLocal(l) + (l && l.cep ? " | CEP " + l.cep : "") +
      (l && (l.latitude || l.longitude) ? ` | (${l.latitude}, ${l.longitude})` : "");
    dlg.append(h, info, desc, loc);
    (d.fotos || []).forEach((f) => {
      const img = document.createElement("img");
      img.src = f.url; img.alt = f.nomeArquivo || "Foto da denúncia";
      dlg.appendChild(img);
    });
    const fechar = document.createElement("button");
    fechar.className = "btn sec"; fechar.textContent = "Fechar";
    fechar.addEventListener("click", () => dlg.close());
    dlg.appendChild(fechar);
    dlg.showModal();
  } catch (e) {
    mostrarMensagem(erroAmigavel(e), "erro");
  }
}

/* ---------- Nova denúncia ---------- */
async function carregarTipos() {
  const sel = document.querySelector('#formDenuncia [name="tipoOcorrencia"]');
  if (sel.dataset.pronto) return;
  let opcoes;
  try {
    const tipos = await listarTipos(); // [{id, nome}]
    opcoes = tipos.map((t) => `<option value="${t.id}">${t.nome}</option>`);
    sel.dataset.pronto = "1";
  } catch {
    opcoes = TIPOS_SCHEMA.map((n) => `<option value="">${n}</option>`);
  }
  sel.innerHTML = '<option value="">Selecione...</option>' + opcoes.join("");
}

function numeroOuNulo(v) {
  const n = parseFloat(String(v).replace(",", "."));
  return Number.isNaN(n) ? null : n;
}

function validar(form, regras) {
  let ok = true;
  form.querySelectorAll(".erro-campo").forEach((e) => e.remove());
  form.querySelectorAll(".invalido").forEach((e) => e.classList.remove("invalido"));
  for (const [nome, checar] of Object.entries(regras)) {
    const campo = form.elements[nome];
    const erro = checar(campo.value.trim());
    if (erro) {
      ok = false;
      campo.classList.add("invalido");
      const span = document.createElement("span");
      span.className = "erro-campo"; span.textContent = erro;
      campo.parentElement.appendChild(span);
    }
  }
  return ok;
}
const obrigatorio = (msg) => (v) => (v ? "" : msg);
const emailValido = (v) => (/^\S+@\S+\.\S+$/.test(v) ? "" : "Informe um e-mail válido.");

// POST criarDenuncia — corpo segue as entidades Denuncia, Localizacao e Foto
async function enviarDenuncia(ev) {
  ev.preventDefault();
  const f = ev.target;
  if (!validar(f, {
    tipoOcorrencia: obrigatorio("Escolha o tipo do problema."),
    descricao: obrigatorio("Descreva o problema.")
  })) return;

  const e = f.elements;
  const usuario = JSON.parse(localStorage.getItem("aguaAlertaUsuario") || "null");
  if (!usuario?.id) {
    mostrarMensagem("Faça login antes de registrar uma denúncia.", "erro");
    location.hash = "#conta";
    return;
  }
  const corpo = {
    titulo: e.titulo.value.trim(),
    descricao: e.descricao.value.trim(),
    usuario: { id: Number(usuario.id) },
    tipoOcorrencia: { id: Number(e.tipoOcorrencia.value) },
    localizacao: {
      endereco: e.endereco.value.trim(), bairro: e.bairro.value.trim(), cidade: e.cidade.value.trim(),
      estado: e.estado.value.trim(), cep: e.cep.value.trim(),
      latitude: 0,
      longitude: 0
    },
    fotos: e.fotoUrl.value.trim()
      ? [{ nomeArquivo: e.fotoUrl.value.trim().split("/").pop() || "foto", url: e.fotoUrl.value.trim() }]
      : []
  };
  await comCarregando(f, async () => {
    await criarDenuncia(corpo);
    f.reset();
    mostrarMensagem("Denúncia registrada com sucesso!", "ok");
  });
}

/* Geolocalização removida: o formulário agora usa apenas endereço e CEP. */

/* ---------- Conta ---------- */
// POST cadastrarUsuario — campos da entidade Usuario
async function enviarCadastro(ev) {
  ev.preventDefault();
  const f = ev.target;
  if (!validar(f, {
    nome: obrigatorio("Informe seu nome."),
    email: emailValido,
    senha: (v) => (v.length >= 6 ? "" : "A senha precisa ter pelo menos 6 caracteres."),
    cpf: (v) => (!v || /^\d{11}$/.test(v) ? "" : "O CPF deve ter 11 números.")
  })) return;
  const e = f.elements;
  await comCarregando(f, async () => {
    await cadastrarUsuario({
      nome: e.nome.value.trim(), email: e.email.value.trim(), senha: e.senha.value,
      cpf: e.cpf.value.trim() || null, telefone: e.telefone.value.trim() || null
    });
    f.reset();
    mostrarMensagem("Cadastro realizado! Agora você já pode entrar.", "ok");
  });
}

// POST login
async function enviarLogin(ev) {
  ev.preventDefault();
  const f = ev.target;
  if (!validar(f, { email: emailValido, senha: obrigatorio("Informe a senha.") })) return;
  await comCarregando(f, async () => {
    const usuario = await login({ email: f.elements.email.value.trim(), senha: f.elements.senha.value });
    localStorage.setItem("aguaAlertaUsuario", JSON.stringify(usuario));
    mostrarMensagem("Login realizado!", "ok");
    location.hash = "#perfil";
  }, "Não foi possível entrar. Confira e-mail e senha.");
}

// Mostra "carregando" no botão e transforma erros em mensagens claras
async function comCarregando(form, acao, erroPadrao) {
  const btn = form.querySelector('[type="submit"]');
  const textoOriginal = btn.textContent;
  btn.disabled = true; btn.textContent = "Aguarde...";
  limparMensagem();
  try {
    await acao();
  } catch (e) {
    const semRota = e instanceof ApiIndisponivelError;
    mostrarMensagem(semRota || !erroPadrao ? erroAmigavel(e) : erroPadrao, "erro");
  } finally {
    btn.disabled = false; btn.textContent = textoOriginal;
  }
}

/* ---------- Inicialização ---------- */
document.addEventListener("DOMContentLoaded", () => {
  $("#aviso-api").hidden = Object.keys(ENDPOINTS).some((n) => apiDisponivel(n));
  $("#menuBtn").addEventListener("click", () => {
    const aberto = $("#menu").classList.toggle("aberto");
    $("#menuBtn").setAttribute("aria-expanded", aberto);
  });
  $("#formDenuncia").addEventListener("submit", enviarDenuncia);
  $("#formCadastro").addEventListener("submit", enviarCadastro);
  $("#formLogin").addEventListener("submit", enviarLogin);
  window.addEventListener("hashchange", navegar);
  navegar();
});
