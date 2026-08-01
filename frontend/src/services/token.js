const CHAVE = 'food-delivery.token'

export function salvarToken(token) {
  localStorage.setItem(CHAVE, token)
}

export function lerToken() {
  return localStorage.getItem(CHAVE)
}

export function removerToken() {
  localStorage.removeItem(CHAVE)
}

export function estaAutenticado() {
  return Boolean(lerToken())
}
