const API_BASE_URL = "http://localhost:8080";

const ENDPOINTS = {
  listarDenuncias:   { metodo: "GET",    caminho: "/api/denuncias" },
  buscarDenuncia:    { metodo: "GET",    caminho: "/api/denuncias/{id}" },
  criarDenuncia:     { metodo: "POST",   caminho: "/api/denuncias" },
  atualizarDenuncia: { metodo: "PUT",    caminho: "/api/denuncias/{id}" },
  excluirDenuncia:   { metodo: "DELETE", caminho: "/api/denuncias/{id}" },
  listarTipos:       { metodo: "GET",    caminho: "/api/tipos-ocorrencia" },
  cadastrarUsuario:  { metodo: "POST",   caminho: "/api/usuarios" },
  login:             { metodo: "POST",   caminho: "/api/usuarios/login" },
  buscarUsuario:     { metodo: "GET",    caminho: "/api/usuarios/{id}" },
  atualizarUsuario:  { metodo: "PUT",    caminho: "/api/usuarios/{id}" }
};

class ApiIndisponivelError extends Error {}
function apiDisponivel(nome) { return Boolean(ENDPOINTS[nome]?.caminho); }

async function requisitar(nome, { id, corpo } = {}) {
  const ep = ENDPOINTS[nome];
  if (!ep?.caminho) throw new ApiIndisponivelError("Rota não configurada.");
  const url = API_BASE_URL + ep.caminho.replace("{id}", id ?? "");
  let resposta;
  try {
    resposta = await fetch(url, {
      method: ep.metodo,
      headers: corpo ? { "Content-Type": "application/json" } : undefined,
      body: corpo ? JSON.stringify(corpo) : undefined
    });
  } catch {
    throw new Error("Não foi possível conectar ao back-end. Inicie o Spring Boot na porta 8080.");
  }
  const texto = await resposta.text();
  let dados = null;
  if (texto) { try { dados = JSON.parse(texto); } catch { dados = texto; } }
  if (!resposta.ok) throw new Error(mensagemDeErro(resposta.status, dados));
  return dados;
}

function mensagemDeErro(status, dados) {
  if (dados && typeof dados === "object" && dados.message) return dados.message;
  if (status === 400) return "Os dados enviados são inválidos. Confira o formulário.";
  if (status === 401 || status === 403) return "E-mail ou senha inválidos.";
  if (status === 404) return "Item não encontrado.";
  if (status === 409) return "Já existe um cadastro com esses dados.";
  return "O servidor teve um problema. Tente novamente.";
}

const listarDenuncias = () => requisitar("listarDenuncias");
const buscarDenuncia = (id) => requisitar("buscarDenuncia", { id });
const excluirDenuncia = (id) => requisitar("excluirDenuncia", { id });
const atualizarDenuncia = (id, corpo) => requisitar("atualizarDenuncia", { id, corpo });
const criarDenuncia = (corpo) => requisitar("criarDenuncia", { corpo });
const listarTipos = () => requisitar("listarTipos");
const cadastrarUsuario = (corpo) => requisitar("cadastrarUsuario", { corpo });
const login = (corpo) => requisitar("login", { corpo });
const buscarUsuario = (id) => requisitar("buscarUsuario", { id });
const atualizarUsuario = (id, corpo) => requisitar("atualizarUsuario", { id, corpo });
