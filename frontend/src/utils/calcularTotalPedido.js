export function calcularTotalPedido(pedido) {
  return pedido.itens.reduce((soma, item) => soma + item.quantidade * item.preco, 0)
}
