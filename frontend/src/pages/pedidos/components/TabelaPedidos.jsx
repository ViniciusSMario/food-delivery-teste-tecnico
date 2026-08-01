import Paper from '@mui/material/Paper'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TableRow from '@mui/material/TableRow'
import LinhaPedido from './LinhaPedido'

export default function TabelaPedidos({ pedidos, atualizandoId, onAlterarStatus }) {
  return (
    <TableContainer component={Paper} variant="outlined">
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>#</TableCell>
            <TableCell>Cliente</TableCell>
            <TableCell>Endereço</TableCell>
            <TableCell>Itens</TableCell>
            <TableCell align="right">Total</TableCell>
            <TableCell>Status</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {pedidos.map((pedido) => (
            <LinhaPedido
              key={pedido.id}
              pedido={pedido}
              atualizando={atualizandoId === pedido.id}
              onAlterarStatus={onAlterarStatus}
            />
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}
