import Link from '@mui/material/Link'
import TableCell from '@mui/material/TableCell'
import TableRow from '@mui/material/TableRow'
import Typography from '@mui/material/Typography'
import { Link as RouterLink } from 'react-router-dom'
import { calcularTotalPedido } from '../../../utils/calcularTotalPedido'
import { formatarMoeda } from '../../../utils/formatarMoeda'
import SeletorStatus from './SeletorStatus'

export default function LinhaPedido({ pedido, atualizando, onAlterarStatus }) {
  const total = calcularTotalPedido(pedido)
  const resumoItens = pedido.itens.map((item) => `${item.quantidade}x ${item.nome}`).join(', ')

  return (
    <TableRow hover>
      <TableCell>
        <Link component={RouterLink} to={`/pedidos/${pedido.id}`}>
          #{pedido.id}
        </Link>
      </TableCell>
      <TableCell>{pedido.cliente}</TableCell>
      <TableCell>{pedido.enderecoEntrega}</TableCell>
      <TableCell>
        <Typography variant="body2" color="text.secondary">
          {resumoItens}
        </Typography>
      </TableCell>
      <TableCell align="right">{formatarMoeda(total)}</TableCell>
      <TableCell>
        <SeletorStatus
          status={pedido.status}
          desabilitado={atualizando}
          onAlterar={(novoStatus) => onAlterarStatus(pedido.id, novoStatus)}
        />
      </TableCell>
    </TableRow>
  )
}
