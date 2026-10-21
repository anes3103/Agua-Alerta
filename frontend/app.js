/* app.js — interface (DOM, eventos, validação). Chamadas à API ficam em api.js */

// Nomes de exibição do enum StatusDenuncia do back-end
const STATUS = {
  PENDENTE: "Pendente", EM_ANALISE: "Em análise", EM_ANDAMENTO: "Em andamento",
  RESOLVIDA: "Resolvida", CANCELADA: "Cancelada"
};
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
  document.querySelectorAll("nav a").forEach((a) => {
    const ativo = a.hash === "#" + alvo;
    a.classList.toggle("ativo", ativo);
    if (ativo) a.setAttribute("aria-current", "page"); else a.removeAttribute("aria-current");
  });
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
  lista.setAttribute("aria-busy", "true");
  lista.innerHTML = '<div class="card esqueleto" aria-hidden="true"></div>'.repeat(3);
  try {
    const dados = await listarDenuncias();
    lista.removeAttribute("aria-busy");
    if (!dados || dados.length === 0) {
      lista.innerHTML = '<p class="vazio">Nenhuma denúncia registrada ainda. <a href="#nova">Seja o primeiro a registrar.</a></p>';
      return;
    }
    lista.innerHTML = "";
    dados.forEach((d) => lista.appendChild(criarCard(d)));
  } catch (e) {
    lista.removeAttribute("aria-busy");
    lista.innerHTML = "";
    mostrarMensagem(erroAmigavel(e), "erro");
  }
}

function textoLocal(loc) {
  if (!loc) return "Local não informado";
  return [loc.endereco, loc.bairro, loc.cidade && loc.estado ? `${loc.cidade}/${loc.estado}` : loc.cidade]
    .filter(Boolean).join(", ") || "Local não informado";
}

// Linha de informação com ícone decorativo
function linhaInfo(icone, texto) {
  const el = document.createElement("small");
  el.className = "info";
  const ic = document.createElement("span");
  ic.className = "icone"; ic.setAttribute("aria-hidden", "true"); ic.textContent = icone;
  el.append(ic, document.createTextNode(texto));
  return el;
}

function criarCard(d) {
  const card = document.createElement("article");
  card.className = "card";
  const topo = document.createElement("div");
  topo.className = "card-topo";
  const status = document.createElement("span");
  status.className = "status " + d.status;
  status.textContent = STATUS[d.status] || d.status;
  topo.appendChild(status);
  if (d.data) {
    const data = document.createElement("small");
    data.className = "data";
    data.textContent = new Date(d.data).toLocaleDateString("pt-BR");
    topo.appendChild(data);
  }
  const titulo = document.createElement("h3");
  titulo.textContent = d.titulo || "Sem título";
  const botao = document.createElement("button");
  botao.className = "btn sec";
  botao.textContent = "Ver detalhes";
  botao.addEventListener("click", () => abrirDetalhe(d.id));
  card.append(topo, titulo);
  if (d.tipoOcorrencia && d.tipoOcorrencia.nome) card.appendChild(linhaInfo("💧", d.tipoOcorrencia.nome));
  card.append(linhaInfo("📍", textoLocal(d.localizacao)), botao);
  return card;
}

/* ---------- Denúncias: detalhe (GET buscarDenuncia) ---------- */
async function confirmarExclusaoDenuncia(id, titulo) {
  const tituloTexto = (titulo || "Sem título").trim();
  const confirmar = window.confirm(`Deseja realmente apagar a denúncia "${tituloTexto}"?`);
  if (!confirmar) return;

  try {
    await excluirDenuncia(id);
    const dlg = $("#detalhe");
    if (dlg && dlg.open) dlg.close();
    mostrarMensagem("Denúncia apagada com sucesso.", "ok");
    if (location.hash === "#denuncias") carregarDenuncias();
  } catch (e) {
    mostrarMensagem(erroAmigavel(e), "erro");
  }
}

async function abrirDetalhe(id) {
  try {
    const d = await buscarDenuncia(id);
    const dlg = $("#detalhe");
    dlg.innerHTML = "";
    const selo = document.createElement("span");
    selo.className = "status " + d.status;
    selo.textContent = STATUS[d.status] || d.status;
    const h = document.createElement("h2");
    h.textContent = d.titulo || "Sem título";
    const info = document.createElement("p");
    info.className = "meta";
    const data = d.data ? new Date(d.data).toLocaleDateString("pt-BR") : "";
    info.textContent = [d.tipoOcorrencia && d.tipoOcorrencia.nome, data].filter(Boolean).join(" | ");
    const desc = document.createElement("p");
    desc.className = "detalhe-desc";
    desc.textContent = d.descricao;
    const loc = document.createElement("p");
    loc.className = "detalhe-local";
    const l = d.localizacao;
    loc.textContent = "Local: " + textoLocal(l) + (l && l.cep ? " | CEP " + l.cep : "") +
      (l && (l.latitude || l.longitude) ? ` | (${l.latitude}, ${l.longitude})` : "");
    dlg.append(selo, h, info, desc, loc);
    (d.fotos || []).forEach((f) => {
      const img = document.createElement("img");
      img.src = f.url; img.alt = f.nomeArquivo || "Foto da denúncia";
      dlg.appendChild(img);
    });
    const excluir = document.createElement("button");
    excluir.className = "btn sec";
    excluir.textContent = "Apagar denúncia";
    excluir.addEventListener("click", () => confirmarExclusaoDenuncia(d.id, d.titulo));
    const fechar = document.createElement("button");
    fechar.className = "btn sec"; fechar.textContent = "Fechar";
    fechar.addEventListener("click", () => dlg.close());
    dlg.append(excluir, fechar);
    dlg.showModal();
  } catch (e) {
    mostrarMensagem(erroAmigavel(e), "erro");
  }
}

/* ---------- Nova denúncia ---------- */
async function carregarTipos() {
  const sel = document.querySelector('#formDenuncia [name="tipoOcorrencia"]');
  if (sel.dataset.pronto) return;
  sel.disabled = true;
  sel.innerHTML = '<option value="">Carregando tipos...</option>';
  try {
    const tipos = await listarTipos(); // [{id, nome}]
    sel.replaceChildren(new Option("Selecione...", ""));
    tipos.forEach((tipo) => sel.add(new Option(tipo.nome, tipo.id)));
    if (tipos.length === 0) {
      sel.replaceChildren(new Option("Nenhum tipo cadastrado.", ""));
      mostrarMensagem("O back-end ainda não possui tipos de ocorrência cadastrados.", "erro");
      return;
    }
    sel.dataset.pronto = "1";
    sel.disabled = false;
  } catch (e) {
    sel.replaceChildren(new Option("Não foi possível carregar os tipos.", ""));
    mostrarMensagem(erroAmigavel(e), "erro");
  }
}

function numeroOuNulo(v) {
  const n = parseFloat(String(v).replace(",", "."));
  return Number.isNaN(n) ? null : n;
}

const semAcento = (t) => String(t || "").normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase();

const REGRAS_TIPO = [
  ["esgoto", "esgoto"],
  ["contamin", "contaminada"],
  ["falta", "falta"],
  ["desabastec", "falta"],
  ["polu", "rios"],
  ["manancial", "rios"],
  ["vazamento", "vazamento"]
];

function lerAnalise(texto) {
  const analise = {};
  String(texto).split("\n").forEach((linha) => {
    const i = linha.indexOf(":");
    if (i === -1) return;
    analise[linha.slice(0, i).trim().toLowerCase()] = linha.slice(i + 1).trim();
  });
  return analise;
}

function escolherTipo(tipoIA, sel) {
  const texto = semAcento(tipoIA);
  for (const [palavraIA, palavraOpcao] of REGRAS_TIPO) {
    if (!texto.includes(palavraIA)) continue;
    const opcao = [...sel.options].find((o) => semAcento(o.text).includes(palavraOpcao));
    if (opcao) return opcao;
  }
  return null;
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
  const usuario = obterUsuarioSessao();
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
        $("#analiseIA").hidden = true;
    mostrarMensagem("Denúncia registrada com sucesso!", "ok");
  });
}

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
  });
}

function obterUsuarioSessao() {
  try {
    return JSON.parse(localStorage.getItem("aguaAlertaUsuario") || "null");
  } catch {
    localStorage.removeItem("aguaAlertaUsuario");
    return null;
  }
}

function criarCampoPerfil(rotulo, nome, valor, tipo = "text") {
  const label = document.createElement("label");
  label.append(document.createTextNode(rotulo));
  const input = document.createElement("input");
  input.name = nome;
  input.type = tipo;
  input.value = valor || "";
  input.maxLength = {
    nome: 100,
    email: 150,
    cpf: 11,
    telefone: 20,
    senha: 255
  }[nome];
  if (nome === "email" || nome === "nome") input.required = true;
  label.appendChild(input);
  return label;
}

async function carregarPerfil() {
  const conteudo = $("#perfilConteudo");
  conteudo.replaceChildren();
  const sessao = obterUsuarioSessao();
  if (!sessao?.id) {
    const aviso = document.createElement("p");
    aviso.className = "vazio";
    aviso.textContent = "Entre na sua conta para visualizar e atualizar seu perfil.";
    const entrar = document.createElement("a");
    entrar.className = "btn";
    entrar.href = "#conta";
    entrar.textContent = "Entrar";
    conteudo.append(aviso, entrar);
    return;
  }

  conteudo.textContent = "Carregando perfil...";
  try {
    const usuario = await buscarUsuario(sessao.id);
    localStorage.setItem("aguaAlertaUsuario", JSON.stringify(usuario));
    const form = document.createElement("form");
    form.className = "form";
    form.id = "formPerfil";
    form.noValidate = true;
    form.append(
      criarCampoPerfil("Nome *", "nome", usuario.nome),
      criarCampoPerfil("E-mail *", "email", usuario.email, "email"),
      criarCampoPerfil("CPF (somente números)", "cpf", usuario.cpf),
      criarCampoPerfil("Telefone", "telefone", usuario.telefone, "tel"),
      criarCampoPerfil("Nova senha (opcional)", "senha", "", "password")
    );
    const salvar = document.createElement("button");
    salvar.className = "btn";
    salvar.type = "submit";
    salvar.textContent = "Salvar alterações";
    const sair = document.createElement("button");
    sair.className = "btn sec";
    sair.type = "button";
    sair.textContent = "Sair da conta";
    sair.addEventListener("click", sairDaConta);
    form.append(salvar, sair);
    form.addEventListener("submit", atualizarPerfil);
    const topo = document.createElement("div");
    topo.className = "perfil-topo";
    const avatar = document.createElement("span");
    avatar.className = "avatar"; avatar.setAttribute("aria-hidden", "true");
    avatar.textContent = (usuario.nome || "?").trim().charAt(0).toUpperCase();
    const quem = document.createElement("div");
    const nome = document.createElement("strong");
    nome.textContent = usuario.nome || "";
    const mail = document.createElement("small");
    mail.textContent = usuario.email || "";
    quem.append(nome, mail);
    topo.append(avatar, quem);
    conteudo.replaceChildren(topo, form);
  } catch (e) {
    conteudo.replaceChildren();
    mostrarMensagem(erroAmigavel(e), "erro");
  }
}

async function atualizarPerfil(ev) {
  ev.preventDefault();
  const form = ev.currentTarget;
  if (!validar(form, {
    nome: obrigatorio("Informe seu nome."),
    email: emailValido,
    cpf: (v) => (!v || /^\d{11}$/.test(v) ? "" : "O CPF deve ter 11 números."),
    senha: (v) => (!v || v.length >= 6 ? "" : "A senha precisa ter pelo menos 6 caracteres.")
  })) return;

  const sessao = obterUsuarioSessao();
  const campos = form.elements;
  await comCarregando(form, async () => {
    const usuario = await atualizarUsuario(sessao.id, {
      nome: campos.nome.value.trim(),
      email: campos.email.value.trim(),
      cpf: campos.cpf.value.trim() || null,
      telefone: campos.telefone.value.trim() || null,
      senha: campos.senha.value || null
    });
    localStorage.setItem("aguaAlertaUsuario", JSON.stringify(usuario));
    mostrarMensagem("Perfil atualizado com sucesso.", "ok");
    await carregarPerfil();
  });
}

function sairDaConta() {
  localStorage.removeItem("aguaAlertaUsuario");
  location.hash = "#conta";
  mostrarMensagem("Você saiu da sua conta.", "ok");
}

// Mostra "carregando" no botão e transforma erros em mensagens claras
async function comCarregando(form, acao) {
  const btn = form.querySelector('[type="submit"]');
  const textoOriginal = btn.textContent;
  btn.disabled = true; btn.textContent = "Aguarde...";
  limparMensagem();
  try {
    await acao();
  } catch (e) {
    mostrarMensagem(erroAmigavel(e), "erro");
  } finally {
    btn.disabled = false; btn.textContent = textoOriginal;
  }
}

/* ---------- Inicialização ---------- */
document.addEventListener("DOMContentLoaded", () => {
  $("#aviso-api").hidden = true;
  $("#endereco-api").textContent = API_BASE_URL;
  $("#menuBtn").addEventListener("click", () => {
    const aberto = $("#menu").classList.toggle("aberto");
    $("#menuBtn").setAttribute("aria-expanded", aberto);
  });
    $("#btnSugerir").addEventListener("click", sugerirTipo);
  $("#formDenuncia").addEventListener("submit", enviarDenuncia);
  $("#formCadastro").addEventListener("submit", enviarCadastro);
  $("#formLogin").addEventListener("submit", enviarLogin);
  window.addEventListener("hashchange", navegar);
  navegar();
});

async function sugerirTipo() {
  const f = $("#formDenuncia");
  const descricao = f.elements.descricao.value.trim();
  if (!descricao) {
    mostrarMensagem("Escreva a descrição antes de pedir a sugestão.", "erro");
    return;
  }
  const btn = $("#btnSugerir");
  const caixa = $("#analiseIA");
  btn.disabled = true;
  btn.textContent = "Analisando...";
  limparMensagem();
  try {
    const analise = lerAnalise(await analisarDenuncia(descricao));
    if (!analise.tipo) {
      mostrarMensagem("A IA não identificou um problema de água ou esgoto nessa descrição.", "erro");
      caixa.hidden = true;
      return;
    }
    const sel = f.elements.tipoOcorrencia;
    const opcao = escolherTipo(analise.tipo, sel);
    if (opcao) sel.value = opcao.value;
    caixa.textContent = `Sugestão da IA: ${analise.tipo} | Gravidade: ${analise.gravidade || "não informada"} | ${analise.resumo || ""}`;
    if (!opcao) caixa.textContent += " | Escolha o tipo manualmente.";
    caixa.hidden = false;
  } catch (e) {
    mostrarMensagem(erroAmigavel(e) + " Você pode escolher o tipo manualmente.", "erro");
  } finally {
    btn.disabled = false;
    btn.textContent = "Sugerir tipo com IA";
  }
}