import api from './api'

export function login(credenciais) {
  return api.post('/auth/login', credenciais).then((resposta) => resposta.data)
}

export function register(data) {
  return api.post('/auth/register', data).then((resposta) => resposta.data)
}
