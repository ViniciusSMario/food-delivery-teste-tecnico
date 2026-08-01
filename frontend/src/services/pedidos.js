import api from './api'

export function listarPedidos() {
  return api.get('/pedidos').then((resposta) => resposta.data)
}

export function buscarPedido(id) {
  return api.get(`/pedidos/${id}`).then((resposta) => resposta.data)
}

export function criarPedido(dados) {
  return api.post('/pedidos', dados).then((resposta) => resposta.data)
}

export function atualizarStatusPedido(id, status) {
  return api.put(`/pedidos/${id}/status`, { status }).then((resposta) => resposta.data)
}
