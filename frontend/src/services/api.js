import axios from 'axios'
import { lerToken, removerToken } from './token'

const ROTAS_PUBLICAS = ['/auth/login', '/auth/register']

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
})

// Anexa o token em toda requisicao, para nao repetir isso em cada chamada.
api.interceptors.request.use((config) => {
  const token = lerToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

api.interceptors.response.use(
  (resposta) => resposta,
  (erro) => {
    const url = erro.config?.url ?? ''
    const ehLoginOuCadastro = ROTAS_PUBLICAS.some((rota) => url.includes(rota))

    // Token expirado ou invalido: derruba a sessao e volta para o login.
    // O 401 de /auth/** e "senha errada", nao sessao expirada, e por isso
    // precisa passar direto para a tela tratar e exibir a mensagem.
    if (erro.response?.status === 401 && !ehLoginOuCadastro) {
      removerToken()
      window.location.assign('/login')
    }

    return Promise.reject(erro)
  },
)

export default api
